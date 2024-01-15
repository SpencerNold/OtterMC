package launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Launch {
	
	public static boolean launch(File file) {
		try {
			File mcDir = getMinecraftDirectory();
			
			String agent = "-javaagent:" + file.getAbsolutePath();
			String nativePath = "-Djava.library.path=" + getJoinedWithSeparator(mcDir.getAbsolutePath(), "bin", "455edb6b1454a7f3243f37b5f240f69e1b0ce4fa");
			String classPath = getClassPath(mcDir.getAbsolutePath());
			String assetsDir = getJoinedWithSeparator(mcDir.getAbsolutePath(), "assets");
			
			String[] arguments = new String[] { "java", agent, nativePath, "-cp", classPath, "net.minecraft.client.main.Main", "--version", "OtterMC", "--accessToken", "0", "--assetsDir", assetsDir, "--assetIndex", "1.8", "--userProperties", "{}" };
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
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static String getClassPath(String mcDir) {
		List<String> classPath = new ArrayList<>();
		
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "com", "mojang", "netty", "1.8.8", "netty-1.8.8.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "oshi-project", "oshi-core", "1.1", "oshi-core-1.1.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "net", "java", "dev", "jna", "jna", "3.4.0", "jna-3.4.0.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "net", "java", "dev", "jna", "platform", "3.4.0", "platform-3.4.0.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "com", "ibm", "icu", "icu4j-core-mojang", "51.2", "icu4j-core-mojang-51.2.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "net", "sf", "jopt-simple", "jopt-simple", "4.6", "jopt-simple-4.6.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "com", "paulscode", "codecjorbis", "20101023", "codecjorbis-20101023.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "com", "paulscode", "codecwav", "20101023", "codecwav-20101023.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "com", "paulscode", "libraryjavasound", "20101123", "libraryjavasound-20101123.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "com", "paulscode", "librarylwjglopenal", "20100824", "librarylwjglopenal-20100824.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "com", "paulscode", "soundsystem", "20120107", "soundsystem-20120107.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "io", "netty", "netty-all", "4.0.23.Final", "netty-all-4.0.23.Final.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "com", "google", "guava", "guava", "17.0", "guava-17.0.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "org", "apache", "commons", "commons-lang3", "3.3.2", "commons-lang3-3.3.2.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "commons-io", "commons-io", "2.4", "commons-io-2.4.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "commons-codec", "commons-codec", "1.9", "commons-codec-1.9.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "net", "java", "jinput", "jinput", "2.0.5", "jinput-2.0.5.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "net", "java", "jutils", "jutils", "1.0.0", "jutils-1.0.0.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "com", "google", "code", "gson", "gson", "2.2.4", "gson-2.2.4.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "com", "mojang", "authlib", "1.5.21", "authlib-1.5.21.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "com", "mojang", "realms", "1.7.59", "realms-1.7.59.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "org", "apache", "commons", "commons-compress", "1.8.1", "commons-compress-1.8.1.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "org", "apache", "httpcomponents", "httpclient", "4.3.3", "httpclient-4.3.3.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "commons-logging", "commons-logging", "1.1.3", "commons-logging-1.1.3.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "org", "apache", "httpcomponents", "httpcore", "4.3.2", "httpcore-4.3.2.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "org", "apache", "logging", "log4j", "log4j-api", "2.0-beta9", "log4j-api-2.0-beta9.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "org", "apache", "logging", "log4j", "log4j-core", "2.0-beta9", "log4j-core-2.0-beta9.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "org", "lwjgl", "lwjgl", "lwjgl", "2.9.4-nightly-20150209", "lwjgl-2.9.4-nightly-20150209.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "org", "lwjgl", "lwjgl", "lwjgl_util", "2.9.4-nightly-20150209", "lwjgl_util-2.9.4-nightly-20150209.jar"));
		classPath.add(getJoinedWithSeparator(mcDir, "libraries", "tv", "twitch", "twitch", "6.5", "twitch-6.5.jar"));
		
		classPath.add(getJoinedWithSeparator(mcDir, "versions", "1.8.9", "1.8.9.jar"));
		return String.join(File.pathSeparator, classPath);
	}
	
	private static File getMinecraftDirectory() {
		return new File(System.getenv("APPDATA") + File.separator + ".minecraft");
	}
	
	private static String getJoinedWithSeparator(String... paths) {
		return String.join(File.separator, paths);
	}
}