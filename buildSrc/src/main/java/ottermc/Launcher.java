package ottermc;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.gson.*;
import org.gradle.api.GradleScriptException;

public class Launcher {

	public static void attach(File file, int version) {
		String java = getJava8Path(version);
		try {
			Runtime.getRuntime().exec(String.format("%s -jar %s %s", java, file.getAbsolutePath(), VersionRegistry.translateVersionToNameString(version)));
		} catch (IOException e) {
			throw new GradleScriptException("failed to attach jar", e);
		}
	}

	public static void launch(File file, int version) {
		File libDir = new File(file.getParentFile(), "libraries");
		boolean libDirExists = libDir.exists();
		File binDir = new File(file.getParentFile(), "bin");
		boolean binDirExists = binDir.exists();
		File gameJar = new File(file.getParentFile(), "game.jar");
		try {
			File mcDir = getMinecraftDirectory();
			String versionName = VersionRegistry.translateVersionToNameString(version);
			File versionJsonFile = new File(getJoinedWithSeparator(mcDir.getAbsolutePath(), "versions", versionName, versionName + ".json"));
			if (!versionJsonFile.exists())
				throw new GradleScriptException("missing version json file", new FileNotFoundException(versionJsonFile.getAbsolutePath()));
			JsonElement element = new Gson().fromJson(new FileReader(versionJsonFile), JsonElement.class);
			if (!element.isJsonObject())
				throw new GradleScriptException("json is invalid", new JsonParseException("JsonElement is not of type JsonObject"));
			if (!gameJar.exists()) {
				JsonObject client = element.getAsJsonObject().getAsJsonObject("downloads").getAsJsonObject("client");
				String url = client.get("url").getAsString();
				downloadTo(url, new FileOutputStream(gameJar));
			}
			String assetIndex = element.getAsJsonObject().get("assets").getAsString();
			List<String> argList = new ArrayList<>();
			if (element.getAsJsonObject().has("arguments")) {
				// Clean this up as more versions are added
				JsonArray cliArgs = element.getAsJsonObject().get("arguments").getAsJsonObject().get("jvm").getAsJsonArray();
				for (JsonElement e : cliArgs) {
					if (e.isJsonObject() && e.getAsJsonObject().has("rules")) {
						JsonArray rulesArray = e.getAsJsonObject().get("rules").getAsJsonArray();
						JsonObject rules = rulesArray.size() > 1 ? rulesArray.get(1).getAsJsonObject() : rulesArray.get(0).getAsJsonObject();
						if (rules.has("os")) {
							JsonObject os = rules.get("os").getAsJsonObject();
							boolean add = false;
							if (os.has("name")) {
								String osName = os.get("name").getAsString();
								if (osName.equals("osx") && isMacOS())
									add = true;
								else if (osName.equals("windows") && isWindows())
									add = true;
							}
							if (os.has("arch")) {
								String archName = os.get("arch").getAsString();
								if (archName.equals("x86") && is32Bit())
									add = true;
							}
							if (add) {
								JsonElement value = e.getAsJsonObject().get("value");
								if (value.isJsonArray()) {
									for (JsonElement e1 : value.getAsJsonArray())
										argList.add(e1.getAsString());
								} else if (value.isJsonPrimitive()) {
									argList.add(value.getAsString());
								}
							}
						}
					}
				}
			}
			JsonArray libraries = element.getAsJsonObject().get("libraries").getAsJsonArray();
			List<String> classPath = new ArrayList<>();
			for (JsonElement e : libraries) {
				JsonObject lib = e.getAsJsonObject();
				JsonObject downloads = lib.getAsJsonObject("downloads");
				if (downloads.has("artifact")) {
					if (libDirExists)
						continue;
					JsonObject artifact = downloads.getAsJsonObject("artifact");
					String path = artifact.get("path").getAsString();
					String url = artifact.get("url").getAsString();
					File lfile = new File(libDir, path.replace("/", File.separator));
					lfile.getParentFile().mkdirs();
					downloadTo(url, new FileOutputStream(lfile));
					classPath.add(lfile.getAbsolutePath());
				} else {
					if (binDirExists)
						continue;
					JsonObject classifiers = downloads.getAsJsonObject("classifiers");
					JsonElement dylib = classifiers.get(getOSClassifier(false));
					dylib = dylib == null ? classifiers.get(getOSClassifier(true)) : dylib;
					if (dylib != null) {
						String[] path = dylib.getAsJsonObject().get("path").getAsString().split("/");
						String name = path[path.length - 1];
						String url = dylib.getAsJsonObject().get("url").getAsString();
						File dfile = new File(binDir, name);
						dfile.getParentFile().mkdirs();
						downloadTo(url, new FileOutputStream(dfile));
						extractTo(dfile);
						dfile.delete();
					}
				}
			}
			classPath.add(gameJar.getAbsolutePath());
			if (libDirExists)
				walk(libDir, f -> classPath.add(f.getAbsolutePath()));
			String agent = "-javaagent:" + file.getAbsolutePath();
			String nativePath = "-Djava.library.path=" + binDir.getAbsolutePath();
			String assetsDir = getJoinedWithSeparator(mcDir.getAbsolutePath(), "assets");

            List<String> arguments = new ArrayList<>(Arrays.asList(getJava8Path(version), agent, nativePath));
			arguments.addAll(argList);
			arguments.addAll(Arrays.asList("-cp", String.join(File.pathSeparator, classPath), "net.minecraft.client.main.Main"));
			arguments.addAll(Arrays.asList("--version", "OtterMC", "--accessToken", "0", "--assetsDir", assetsDir, "--assetIndex", assetIndex, "--userProperties", "{}"));

			Process process = new ProcessBuilder(arguments).directory(mcDir).start();
			new Thread(() -> {
				Scanner scanner = new Scanner(process.getErrorStream());
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					System.err.println(line);
				}
			}).start();
			Scanner scanner = new Scanner(process.getInputStream());
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);
			}
		} catch (Exception e) {
			// TODO Clean here?
			e.printStackTrace();
			throw new GradleScriptException("client launch failed: " + e.getClass().getName(), e);
		}
	}
	
	private static void downloadTo(String link, OutputStream output) throws IOException {
		URL url = new URL(link);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream input = connection.getInputStream();
		copy(input, output);
		output.flush();
		output.close();
	}

	private static void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[4096];
		int n;
		while ((n = input.read(buffer)) != -1)
			output.write(buffer, 0, n);
	}

	private static void extractTo(File file) throws IOException {
		JarFile jar = new JarFile(file);
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			File f = new File(file.getParentFile(), entry.getName().replace("/", File.separator));
			if (entry.isDirectory()) {
				f.mkdirs();
				continue;
			}
			InputStream input = jar.getInputStream(entry);
			OutputStream output = new FileOutputStream(f);
			copy(input, output);
			output.flush();
			output.close();
		}
		jar.close();
	}

	private static void walk(File file, Consumer<File> consumer) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files)
					walk(f, consumer);
			}
		}
		consumer.accept(file);
	}

	private static String getOSClassifier(boolean architecture) {
		String os = System.getProperty("os.name").toUpperCase();
		boolean x64 = System.getProperty("os.arch").contains("64");
		if (os.contains("WIN"))
			return "natives-windows" + (architecture ? (x64 ? "-64" : "-32") : "");
		else if (os.contains("MAC"))
			return "natives-osx";
		else
			return "natives-linux";
	}

	private static boolean is32Bit() {
		return !System.getProperty("os.arch").contains("64");
	}

	private static boolean isWindows() {
		String os = System.getProperty("os.name").toUpperCase();
		return os.contains("WIN");
	}

	private static boolean isMacOS() {
		String os = System.getProperty("os.name").toUpperCase();
		return os.contains("MAC");
	}

	private static boolean isLinux() {
		String os = System.getProperty("os.name").toUpperCase();
		return !isWindows() && !isMacOS(); // Meh
	}

	private static String getJava8Path(int version) {
		if (isWindows())
			return getJava8PathWIN();
		else if (isMacOS())
			return getJava8PathOSX(version);
		return getJava8PathUNIX();
	}

	private static String getJava8PathWIN() {
		return "java"; // Does not seem to matter if java8 or java17 on windows?
	}

	private static String getJava8PathOSX(int version) {
        try {
            Process process = Runtime.getRuntime().exec("/usr/libexec/java_home -v " + VersionRegistry.JVMS.get(version));
			Scanner scanner = new Scanner(process.getInputStream());
			String path = scanner.hasNextLine() ? (scanner.nextLine() + "/bin/java") : "java";
			scanner.close();
			return path;
        } catch (IOException e) {
            return "java";
        }
	}

	private static String getJava8PathUNIX() {
		return "java"; // ???
	}

	static File getMinecraftDirectory() {
		if (isWindows())
			return getMinecraftDirectoryWIN();
		else if (isMacOS())
			return getMinecraftDirectoryOSX();
		return getMinecraftDirectoryUNIX();
	}

	private static File getMinecraftDirectoryWIN() {
		return new File(getJoinedWithSeparator(System.getenv("APPDATA"), ".minecraft"));
	}

	private static File getMinecraftDirectoryOSX() {
		return new File(getJoinedWithSeparator(System.getProperty("user.home"), "Library", "Application Support", "minecraft"));
	}

	private static File getMinecraftDirectoryUNIX() {
		// This is very much probably wrong, but I have no idea where .minecraft is stored on linux systems
		return new File(getJoinedWithSeparator(System.getProperty("user.home"), ".minecraft"));
	}

	private static String getJoinedWithSeparator(String... paths) {
		return String.join(File.separator, paths);
	}
}