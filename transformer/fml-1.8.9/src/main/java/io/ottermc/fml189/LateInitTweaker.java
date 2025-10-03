package io.ottermc.fml189;

import io.ottermc.common.*;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LateInitTweaker implements ITweaker {

    private static final String[] mandatoryClassLoad = new String[]{
            "me.spencernold.transformer.ClassAdapter",
            "io.ottermc.transformer.adapters.MinecraftClassNameAdapter",
            "io.ottermc.transformer.adapters.MinecraftMethodNameAdapter"
    };

    private File clientDir;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profileName) {
        this.clientDir = new File(clientDir, "ottermc");
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
        File clientJar = new File(clientDir, "client-v1.8.9.jar");
        try {
            launchClassLoader.addURL(clientJar.toURI().toURL());
            JarFile jar = new JarFile(clientJar);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    String className = entry.getName().substring(0, name.length() - 6).replace('/', '.');
                    try {
                        launchClassLoader.findClass(className);
                    } catch (ClassNotFoundException e) {
                        System.out.println("failed to force the loading of: " + className);
                    }
                }
            }
            jar.close();
            //for (String className : mandatoryClassLoad)
            //    Class.forName(className, true, launchClassLoader);

            Client client = new ClientFactory(Launch.classLoader)
                    .setPlugins(new String[]{"pvp-v1.8.9"})
                    .setPluginLoader(new PluginLoader(Launch.classLoader, file -> {
                        try {
                            Launch.classLoader.addURL(file.toURI().toURL());
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    }))
                    .setClassLoader(Launch.classLoader)
                    .create();
            TransformerRegistry registry = client.getRegistry();
            Initializer initializer = client.getClient();
            Map<Object, Object> plugins = client.getPluginMap();
            for (Class<?> clazz : registry.getTransformerRegistry())
                OMCTransformer.register(clazz);
            StateRegistry.setState(launchClassLoader, StateRegistry.getInitState(launchClassLoader));
            initializer.start();
            for (Object implementation : plugins.values())
                new Implementation(implementation).onEnable();
            StateRegistry.setState(launchClassLoader, StateRegistry.getPostInitState(launchClassLoader));

            // Register as FIRST transformer
            launchClassLoader.registerTransformer("io.ottermc.fml189.OMCTransformer");
            List<IClassTransformer> transformers = new ArrayList<>(launchClassLoader.getTransformers());
            for (IClassTransformer transformer : transformers)
                System.out.println(transformer.getClass().getName());
            transformers.remove(OMCTransformer.getInstance());
            transformers.add(0, OMCTransformer.getInstance());
            setTransformers(launchClassLoader, transformers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setTransformers(LaunchClassLoader loader, List<IClassTransformer> list) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = loader.getClass();
        Field field = clazz.getDeclaredField("transformers");
        field.setAccessible(true);
        field.set(loader, list);
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
