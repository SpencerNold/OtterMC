import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import me.spencernold.kwaf.logger.Logger;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Loader {

    private static final ArrayList<String> SUPPORTED_MAIN_CLASSES = new ArrayList<>(Arrays.asList(
            "net.minecraft.client.main.Main", // Vanilla
            "net.minecraft.launchwrapper.Launch", // Old Forge & OptiFine
            "net.minecraftforge.bootstrap.ForgeBootstrap", // New Forge
            "net.fabricmc.loader.impl.launch.knot.KnotClient" // Fabric
    ));

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
                System.out.println("attaching to " + mainClass + " version " + arguments.get("version") + " v" + args[0]);
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
        return SUPPORTED_MAIN_CLASSES.contains(mainClass) && arguments.containsKey("version") && arguments.get("version").contains(version);
    }

    private static File getJarFile() {
        try {
            return new File(Loader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
