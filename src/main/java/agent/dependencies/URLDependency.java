package agent.dependencies;

import java.io.*;
import java.net.*;

public class URLDependency extends Dependency {

    URLDependency(String name, String url) {
        super(name, getURI(url));
    }

    @Override
    public File unwrap(File directory) throws IOException {
        if (!directory.exists())
            directory.mkdirs();
        File file = new File(directory, name + ".jar");
        if (!file.exists()) {
            URLConnection connection = (URLConnection) uri.toURL().openConnection();
            InputStream input = connection.getInputStream();
            OutputStream output = new FileOutputStream(file);
            copy(input, output);
            input.close();
            output.close();
        }
        return file;
    }

    private static URI getURI(String url) {
        try {
            return new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return null;
        }
    }
}
