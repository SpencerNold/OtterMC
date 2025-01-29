import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Loader {

    enum Target {

        VANILLA("net.minecraft.client.main.Main", "{$version}"),
        OLD_FORGE("", ""),
        FORGE("", ""),
        FABRIC("net.fabricmc.loader.impl.launch.knot.KnotClient", "fabric-loader-.+?-${version}"),
        OLD_OPTIFINE("", ""),
        OPTIFINE("net.minecraft.launchwrapper.Launch", "${version}-OptiFine.*$");

        final String mainClass;
        final String version;
        private Pattern pattern;

        Target(String mainClass, String version) {
            this.mainClass = mainClass;
            this.version = version;
        }

        boolean matches(String version) {
            return pattern.matcher(version).matches();
        }

        static Target get(String name, String version) {
            try {
                Target target = valueOf(name.toUpperCase());
                target.pattern = Pattern.compile(target.version.replace("${version}", version));
                return target;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("missing argument for the game version and/or target");
            return;
        }
        Pattern pattern = Pattern.compile("--(.*?) (.*?)(?= |$)");
        for (VirtualMachineDescriptor vmd : VirtualMachine.list()) {
            Matcher matcher = pattern.matcher(vmd.displayName());
            String mainClass = vmd.displayName().split(" ")[0];
            Map<String, String> arguments = new HashMap<>();
            while (matcher.find())
                arguments.put(matcher.group(1), matcher.group(2));
            Target target = Target.get(args[1], args[0]);
            boolean inject = test(mainClass, target, arguments);
            if (inject) {
                inject(vmd.id(), getJarFile());
                System.out.println("attaching to " + target.name() + " v" + args[0]);
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

    private static boolean test(String mainClass, Target target, Map<String, String> arguments) {
        return target != null && mainClass.equals(target.mainClass) && arguments.containsKey("version") && target.matches(arguments.get("version"));
    }

    private static File getJarFile() {
        try {
            return new File(Loader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
