package agent.dependencies;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public abstract class Dependency {

    protected final String name;
    protected final URI uri;

    protected Dependency(String name, URI uri) {
        this.name = name;
        this.uri = uri;
    }

    protected void copy(InputStream input, OutputStream output) throws IOException {
        int n;
        byte[] buffer = new byte[4096];
        while ((n = input.read(buffer)) != -1)
            output.write(buffer, 0, n);
    }

    public abstract File unwrap(File directory) throws IOException;

    public String getName() {
        return name;
    }

    public URI getURI() {
        return uri;
    }

    public static Dependency getURLDependency(String name, String url) {
        return new URLDependency(name, url);
    }

    public static Dependency getFileDependency(String name, String path) {
        return new FileDependency(name, path);
    }
}
