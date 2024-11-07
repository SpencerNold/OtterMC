package agent.dependencies;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.util.jar.JarFile;

public class DependencyLoader {

    private final File directory;
    private final Instrumentation instrumentation;

    public DependencyLoader(File directory, Instrumentation instrumentation) {
        this.directory = directory;
        this.instrumentation = instrumentation;
    }

    public void add(Dependency dependency) {
        if (dependency == null)
            return;
        try {
            load(dependency.unwrap(directory));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load(File file) throws IOException {
        instrumentation.appendToSystemClassLoaderSearch(new JarFile(file));
    }
}
