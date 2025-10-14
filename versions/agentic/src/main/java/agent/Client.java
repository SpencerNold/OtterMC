package agent;

import io.github.ottermc.AbstractSubClient;
import io.github.ottermc.Game;
import io.github.ottermc.logging.Logger;
import io.ottermc.transformer.ClassTransformer;
import io.github.ottermc.StateRegistry;
import io.github.ottermc.c2.ServerController;
import io.github.ottermc.transformer.TransformerRegistry;
import io.github.ottermc.State;
import io.ottermc.transformer.adapters.MinecraftClassNameAdapter;
import io.ottermc.transformer.adapters.MinecraftFieldNameAdapter;
import io.ottermc.transformer.adapters.MinecraftMethodNameAdapter;
import me.spencernold.transformer.Reflection;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    public static void start(String args, Instrumentation instrumentation) throws Exception {
        Logger.log("Starting OtterMC Agentic Client...");
        me.spencernold.transformer.Logger.instance = new me.spencernold.transformer.Logger() {
            @Override
            public void print(String s) {
                Logger.log(s);
            }
        };
        me.spencernold.kwaf.logger.Logger.Companion.setSystemLogger(new me.spencernold.kwaf.logger.Logger() {
            @Override
            public void log(@NotNull Severity severity, @NotNull String s) {
                Logger.log("[" + severity.name() + "] " + s);
            }
        });

        Reflection reflection = new Reflection(MinecraftClassNameAdapter.class, MinecraftMethodNameAdapter.class, MinecraftFieldNameAdapter.class);
        Reflection.setSystemReflectClass(reflection);

        File file = getPathOfJar();
        File dir = file.getParentFile();
        TransformerRegistry registry = new TransformerRegistry();
        Class<?> main = Class.forName("io.github.ottermc.SubClient");
        Constructor<?> constructor = main.getDeclaredConstructor(File.class, TransformerRegistry.class);
        AbstractSubClient client = (AbstractSubClient) constructor.newInstance(dir, registry);
        Game.game = new Game(client);
        ClassTransformer transformer = new ClassTransformer(instrumentation);
        registry.forEach(transformer::register);
        transformer.execute();
        transformer.clear();
        StateRegistry.setState(State.INIT);
        Game.game.start();
        StateRegistry.setState(State.POST_INIT);
    }

    private static File getPathOfJar() throws URISyntaxException {
        URI uri = Client.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        String value = String.valueOf(uri);
        Pattern pattern = Pattern.compile("jar:(.+?)!.*$");
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches())
            return new File(URI.create(matcher.group(1)));
        return new File(uri);
    }
}
