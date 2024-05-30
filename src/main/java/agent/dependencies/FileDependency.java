package agent.dependencies;

import java.io.*;
import java.net.URI;
import java.nio.file.Paths;

public class FileDependency extends Dependency {

    FileDependency(String name, String path) {
        super(name, getURI(path));
    }

    @Override
    public File unwrap(File directory) throws IOException {
        if (!directory.exists())
            directory.mkdirs();
        File file = Paths.get(uri).toFile();
        File dependency = new File(directory, name + ".jar");
        if (dependency.exists())
            return dependency;
        if (file.getAbsolutePath().equals(dependency.getAbsolutePath()))
            return dependency;
        InputStream input = new FileInputStream(file);
        OutputStream output = new FileOutputStream(dependency);
        copy(input, output);
        input.close();
        output.close();
        return dependency;
    }

    private static URI getURI(String path) {
        return new File(path).toURI();
    }
}
