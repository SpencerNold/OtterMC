package io.github.ottermc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarClassLoader extends URLClassLoader {

    private JarClassLoader(URL[] urls, ClassLoader loader) {
        super(urls, loader);
    }

    public static JarClassLoader create(File... files) throws MalformedURLException {
        URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++)
            urls[i] = files[i].toURI().toURL();
        return new JarClassLoader(urls, JarClassLoader.class.getClassLoader());
    }
}
