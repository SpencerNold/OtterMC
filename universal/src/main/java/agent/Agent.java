package agent;

import io.github.ottermc.ClassTransformer;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Initializer;
import io.github.ottermc.api.Plugin;
import io.github.ottermc.c2.ServerController;
import io.github.ottermc.logging.Logger;
import io.ottermc.transformer.State;
import io.ottermc.transformer.TransformerRegistry;

import java.lang.instrument.Instrumentation;
import java.util.Map;

public class Agent {

    private static boolean injectionLoad = false;

    public static void premain(String args, Instrumentation instrumentation) {
        try {
            launch(args, instrumentation);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        injectionLoad = true;
        try {
            launch(args, instrumentation);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private static void launch(String args, Instrumentation instrumentation) throws Exception {
        Client client = new ClientFactory()
                .setPlugins((args == null || args.isEmpty()) ? new String[0] : args.split(","))
                .setPluginLoader(new InstrumentationPluginLoader(instrumentation))
                .setClassLoader(ClassLoader.getSystemClassLoader())
                .create();
        TransformerRegistry registry = client.getRegistry();
        Initializer initializer = client.getClient();
        Map<Plugin, Implementation> plugins = client.getPluginMap();
        ClassTransformer transformer = new ClassTransformer(instrumentation);
        registry.forEach(transformer::register);
        transformer.execute();
        transformer.clear();
        StateRegistry.setState(State.INIT);
        initializer.start();
        for (Implementation implementation : plugins.values())
            implementation.onEnable();
        StateRegistry.setState(State.POST_INIT);
    }

    public static boolean isInjectionLoad() {
        return injectionLoad;
    }
}
