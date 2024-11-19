package io.github.ottermc;

import com.google.gson.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final List<String> SUPPORTED_VERSIONS = Arrays.asList("1.8.9", "1.21.3");

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("usage: <version>");
            return;
        }
        String version = args[0];
        if (!SUPPORTED_VERSIONS.contains(version)) {
            System.err.println("unsupported version");
            return;
        }

        String gameDir = getMinecraftDirectory();
        File clientDir = new File(gameDir, "ottermc");
        if (!clientDir.exists() && !clientDir.mkdir()) {
            System.err.println("failed to create client directory");
            return;
        }
        File jarDir = getJarDirectory();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            // Install client
            File clientSrc = new File(jarDir.getParent(), String.format("client-v%s-remapped-joined.jar", version));
            File clientDst = new File(clientDir, String.format("client-v%s.jar", version));
            copy(clientSrc, clientDst);
            System.out.println("[OtterMC] installed client");

            // Install wrapper
            File wrapperSrc = new File(jarDir.getParent(), "wrapper.jar");
            File wrapperDir = new File(gameDir, String.join(File.separator, "libraries", "io", "github", "ottermc", "wrapper", "1.0.0"));
            if (!wrapperDir.exists() && !wrapperDir.mkdirs()) {
                System.err.println("failed to create library directory");
                return;
            }
            File wrapperDst = new File(wrapperDir, "wrapper-1.0.0.jar");
            copy(wrapperSrc, wrapperDst);
            System.out.println("[OtterMC] installed wrapper");

            // Add Profile
            File profileFile = new File(gameDir, "launcher_profiles.json");
            if (!profileFile.exists()) {
                System.out.println(profileFile.getAbsolutePath());
                System.err.println("missing launcher profile file: try re-running the game");
                return;
            }
            FileReader reader = new FileReader(profileFile);
            JsonElement element = gson.fromJson(reader, JsonElement.class);
            reader.close();
            if (!element.isJsonObject()) {
                System.err.println("malformed launcher_profiles.json");
                return;
            }
            JsonElement profiles = element.getAsJsonObject().get("profiles");
            if (!profiles.isJsonObject()) {
                System.err.println("malformed launcher_profiles.json");
                return;
            }
            profiles.getAsJsonObject().add("ottermc-" + version, gson.fromJson(getProfileJson(version), JsonElement.class));
            FileWriter writer = new FileWriter(profileFile);
            gson.toJson(element, writer);
            writer.close();
            System.out.println("[OtterMC] added profile");

            // Copy Game Jar and Json
            File clientJarDir = new File(gameDir, String.join(File.separator, "versions", "ottermc-" + version));
            if (!clientJarDir.exists() && !clientJarDir.mkdirs()) {
                System.err.println("failed to make client version directory");
                return;
            }
            File gameJarSrc = new File(gameDir, String.join(File.separator, "versions", version, version + ".jar"));
            File gameJarDst = new File(clientJarDir, String.format("ottermc-%s.jar", version));
            copy(gameJarSrc, gameJarDst);
            System.out.println("[OtterMC] copied client jar");

            File gameJsonSrc = new File(gameDir, String.join(File.separator, "versions", version, version + ".json"));
            reader = new FileReader(gameJsonSrc);
            element = gson.fromJson(reader, JsonElement.class);
            reader.close();
            if (!element.isJsonObject()) {
                System.err.println("malformed client.json");
                return;
            }
            JsonObject client = element.getAsJsonObject();
            client.add("id", new JsonPrimitive("ottermc-" + version));
            client.add("mainClass", new JsonPrimitive("io.github.ottermc.Wrapper"));
            JsonElement libraries = client.get("libraries");
            if (!libraries.isJsonArray()) {
                System.err.println("malformed client.json");
                return;
            }
            JsonObject dependency = new JsonObject();
            dependency.add("name", new JsonPrimitive("io.github.ottermc:wrapper:1.0.0"));
            libraries.getAsJsonArray().add(dependency);
            File gameJsonDst = new File(clientJarDir, String.format("ottermc-%s.json", version));
            if (!gameJsonDst.exists() && !gameJsonDst.createNewFile()) {
                System.err.println("failed to create client json");
                return;
            }
            writer = new FileWriter(gameJsonDst);
            gson.toJson(element, writer);
            writer.close();
            System.out.println("[OtterMC] copied and updated client json file");

            // Update Client Json
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copy(File src, File dst) throws IOException {
        if (!dst.exists() && !dst.createNewFile())
            throw new IOException("failed to create: " + dst.getAbsolutePath());
        FileInputStream input = new FileInputStream(src);
        FileOutputStream output = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = input.read(buffer)) != -1)
            output.write(buffer, 0, read);
        input.close();
        output.close();
    }

    private static File getJarDirectory() {
        try {
            return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getProfileJson(String version) {
        return "{\"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAMAAAD04JH5AAAAVFBMVEVHcEz///////////////////////////////////////////////////////////////////////////////////////////////////////////+DS+nTAAAAG3RSTlMA+vLqBBfiVcmzKyE9ENrTCaWZSL40ZG+NhHvWhwKVAAAI2ElEQVR42u1b25arKBANKgKKoCLe8v//eaow6TYGwaRNn5eueZlZ0wmbuu6qIpfLn/zJn/zJn3xA6jr7b2dnVVlIY4y0ZRVAkX0MYSWHVnScd80gabYPs/oMgpqaWShyvV5J3k2mrJfbZusLV6WVhjFjC1qfDoCylqd4PiBIVcNQB1lFafXtFHXRt4KjiiZWnq4A2erk+iVqZNLdFgScoihpnVE5NMr9TcpbVlTnnl/03er8a6JF06BD4IVFMw6moHLk+fI3JFFiLs41gBnV6nw4Is1zckeTazENbOZkhVCca4WyF+n1QQghazhK87WJrlc99mcCKCa+ARCTVLSnAhgf7xeXRPP/DCDNTzbBiwDALz7rhL8MgJpJvQggV6cmorJfR/kRH+Di3Fosm/QlBHkznZuLzQMAAtlPpatUqPNHeITP5lQuYIcHEyS8HSAwyP2289zkD+er1tBzo+DxgLzprbnVvuuVDxYArvWjm/7UUlDL5lHFapK0mLslNEln6tp0ZK2fG2M58NUVLcuisF9SFFDc6SP1rIv1/VypmW1dfgEQ8nKR4htA2s3l4fCyrB+msW1u0o7TPDCo7msEdGMAJCSm+k7Pnakq011fBABXB07Tzy0QC63y1EmeayAYLVR342jOjY2220qQion14m4WNTE26XUhGuN0pCoYkFyu3dlJQu6SJAADqnvXjP1iyErOT4WApJrzb0YC/7GGSPLGxhi+NcMoVKDAwBHN1APfs8CGX60DRIQBAH1sO52nSbiagCI6pH76tRzovEIGdA9+N4mcHK1pQZhIxlC2EAMAkLx3x++0pn5e3iPGeZ5GkR8EUJdI3l/W6D7za2YmpTTzplbuAcDr38n7KaJnWVYgj6VqSU0e56fnXh8zwCyrJVU/1kpvFGTUnHt9l5CGcknVDwrIW+vzfjbqU6/vjhJ9UdpNqk64JxOC+RuVXM8GQJQYh21UL3rZ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\",\"lastUsed\":\"2024-11-17T10:20:50.887Z\",\"lastVersionId\":\"ottermc-" + version + "\",\"name\":\"OtterMC (" + version + ")\",\"type\":\"custom\"}";
    }

    private static String getMinecraftDirectory() {
        String os = System.getProperty("os.name").toUpperCase();
        if (os.contains("MAC")) {
            return getGameDirDarwin();
        } else if (os.contains("WINDOWS")) {
            return getGameDirWindows();
        } else {
            return getGameDirLinux();
        }
    }

    private static String getGameDirWindows() {
        return System.getenv("APPDATA") + File.separator + ".minecraft";
    }

    private static String getGameDirDarwin() {
        return String.join(File.separator, System.getProperty("user.home"), "Library", "Application Support", "minecraft");
    }

    private static String getGameDirLinux() {
        return System.getProperty("user.home") + File.separator + ".minecraft";
    }
}
