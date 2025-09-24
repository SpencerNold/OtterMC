package agent;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class InstrumentationPluginLoader implements PluginLoader {

    private final Instrumentation instrumentation;

    public InstrumentationPluginLoader(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public void load(File file) throws IOException {
        JarFile jar = new JarFile(file);
        instrumentation.appendToSystemClassLoaderSearch(jar);
        jar.close();
    }
}
