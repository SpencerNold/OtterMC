package io.github.ottermc;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class Profiler {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // args[0] = Minecraft Game Directory
    // args[1] = Java executable from jre/
    public static void main(String[] args) {
        if (args.length != 2)
            System.exit(1);
        File mcDir = new File(args[0]);
        File profileFile = new File(mcDir, "launcher_profiles.json");
        JsonElement element = readJsonFile(profileFile);
        if (element == null)
            System.exit(4);
        int exitCode = addToProfile(mcDir, element, "latest", "21", args[1]);
        if (exitCode != 0)
            System.exit(exitCode + 4);
        exitCode = addToProfile(mcDir, element, "v1.8.9", "8", args[1]);
        if (exitCode != 0)
            System.exit(exitCode + 10);
        boolean result = writeJsonFile(profileFile, element);
        if (!result)
            System.exit(16);
    }

    private static JsonElement readJsonFile(File file) {
        try {
            FileReader reader = new FileReader(file);
            JsonElement element = gson.fromJson(reader, JsonElement.class);
            reader.close();
            return element;
        } catch (IOException e) {
            return null;
        }
    }

    private static int addToProfile(File mcDir, JsonElement element, String version, String javaVersion, String javaPath) {
        if (!element.isJsonObject())
            return 1;
        element = element.getAsJsonObject().get("profiles");
        if (!element.isJsonObject())
            return 2;
        String name = "ottermc-" + version;
        JsonObject object = element.getAsJsonObject();

        JsonObject versionObject = new JsonObject();
        versionObject.add("icon", new JsonPrimitive("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAMAAAD04JH5AAAAVFBMVEVHcEz///////////////////////////////////////////////////////////////////////////////////////////////////////////+DS+nTAAAAG3RSTlMA+vLqBBfiVcmzKyE9ENrTCaWZSL40ZG+NhHvWhwKVAAAI2ElEQVR42u1b25arKBANKgKKoCLe8v//eaow6TYGwaRNn5eueZlZ0wmbuu6qIpfLn/zJn/zJn3xA6jr7b2dnVVlIY4y0ZRVAkX0MYSWHVnScd80gabYPs/oMgpqaWShyvV5J3k2mrJfbZusLV6WVhjFjC1qfDoCylqd4PiBIVcNQB1lFafXtFHXRt4KjiiZWnq4A2erk+iVqZNLdFgScoihpnVE5NMr9TcpbVlTnnl/03er8a6JF06BD4IVFMw6moHLk+fI3JFFiLs41gBnV6nw4Is1zckeTazENbOZkhVCca4WyF+n1QQghazhK87WJrlc99mcCKCa+ARCTVLSnAhgf7xeXRPP/DCDNTzbBiwDALz7rhL8MgJpJvQggV6cmorJfR/kRH+Di3Fosm/QlBHkznZuLzQMAAtlPpatUqPNHeITP5lQuYIcHEyS8HSAwyP2289zkD+er1tBzo+DxgLzprbnVvuuVDxYArvWjm/7UUlDL5lHFapK0mLslNEln6tp0ZK2fG2M58NUVLcuisF9SFFDc6SP1rIv1/VypmW1dfgEQ8nKR4htA2s3l4fCyrB+msW1u0o7TPDCo7msEdGMAJCSm+k7Pnakq011fBABXB07Tzy0QC63y1EmeayAYLVR342jOjY2220qQion14m4WNTE26XUhGuN0pCoYkFyu3dlJQu6SJAADqnvXjP1iyErOT4WApJrzb0YC/7GGSPLGxhi+NcMoVKDAwBHN1APfs8CGX60DRIQBAH1sO52nSbiagCI6pH76tRzovEIGdA9+N4mcHK1pQZhIxlC2EAMAkLx3x++0pn5e3iPGeZ5GkR8EUJdI3l/W6D7za2YmpTTzplbuAcDr38n7KaJnWVYgj6VqSU0e56fnXh8zwCyrJVU/1kpvFGTUnHt9l5CGcknVDwrIW+vzfjbqU6/vjhJ9UdpNqk64JxOC+RuVXM8GQJQYh21UL3rZ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"));
        versionObject.add("lastUsed", new JsonPrimitive(DateTimeFormatter.ISO_INSTANT.format(Instant.now())));
        versionObject.add("lastVersionId", new JsonPrimitive(name));
        versionObject.add("name", new JsonPrimitive("OtterMC (" + version + ")"));
        versionObject.add("type", new JsonPrimitive("custom"));
        versionObject.add("javaDir", new JsonPrimitive(String.join(File.separator, mcDir.getAbsolutePath(), "jre", javaVersion, javaPath)));

        if (object.has(name))
            object.remove(name);
        object.add(name, versionObject);
        return 0;
    }

    private static boolean writeJsonFile(File file, JsonElement element) {
        try {
            FileWriter writer = new FileWriter(file);
            gson.toJson(element, writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
