package agent;

import io.github.ottermc.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.function.Consumer;
import java.util.jar.JarFile;

public class InstrumentationPluginLoader extends PluginLoader {

    public InstrumentationPluginLoader(Instrumentation instrumentation) {
        super(file -> {
            try {
                if (!file.exists())
                    return;
                JarFile jar = new JarFile(file);
                instrumentation.appendToSystemClassLoaderSearch(jar);
                jar.close();
            } catch (IOException e) {
                Logger.error(e);
            }
        });
    }
}
