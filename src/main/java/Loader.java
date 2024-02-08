import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Loader {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("missing argument for the JDK implementation");
            return;
        }
        loadLibrary(new File(args[0] + File.separator + "lib", "tools.jar"));
        Pattern pattern = Pattern.compile("--(.*?) (.*?)(?= |$)");
        for (VirtualMachineDescriptor vmd : VirtualMachine.list()) {
            Matcher matcher = pattern.matcher(vmd.displayName());
            String mainClass = vmd.displayName().split(" ")[0];
            Map<String, String> arguments = new HashMap<>();
            while (matcher.find())
                arguments.put(matcher.group(1), matcher.group(2));
            boolean inject = test(mainClass, arguments);
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

    private static void loadLibrary(File file) {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(ClassLoader.getSystemClassLoader(), file.toURI().toURL());
        } catch (NoSuchMethodException | IOException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean test(String mainClass, Map<String, String> arguments) {
        return mainClass.equals("net.minecraft.client.main.Main") && arguments.containsKey("version") && arguments.get("version").equals("1.8.9");
    }

    private static File getJarFile() {
        try {
            return new File(Loader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
