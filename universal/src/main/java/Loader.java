import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Loader {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("missing argument for the game version");
            return;
        }
        Pattern pattern = Pattern.compile("--(.*?) (.*?)(?= |$)");
        for (VirtualMachineDescriptor vmd : VirtualMachine.list()) {
            Matcher matcher = pattern.matcher(vmd.displayName());
            String mainClass = vmd.displayName().split(" ")[0];
            Map<String, String> arguments = new HashMap<>();
            while (matcher.find())
                arguments.put(matcher.group(1), matcher.group(2));
            boolean inject = test(mainClass, args[0], arguments);
            if (inject) {
                inject(vmd.id(), getJarFile());
                return;
            }
        }
        System.err.println("failed to find an acceptable running game client");
    }

    private static void inject(String id, File file) {
        try {
            VirtualMachine vm = VirtualMachine.attach(id);
            vm.loadAgent(file.getAbsolutePath(), "OtterMC-v1.0.0");
            vm.detach();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean test(String mainClass, String version, Map<String, String> arguments) {
        return mainClass.equals("net.minecraft.client.main.Main") && arguments.containsKey("version") && arguments.get("version").equals(version);
    }

    private static File getJarFile() {
        try {
            return new File(Loader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
