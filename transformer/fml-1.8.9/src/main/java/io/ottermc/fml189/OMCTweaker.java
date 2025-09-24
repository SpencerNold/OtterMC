package io.ottermc.fml189;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.List;

public class OMCTweaker implements ITweaker {

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
        File client = new File(clientDir, "client-v1.8.9.jar");
        try {
            launchClassLoader.addURL(client.toURI().toURL());
            for (String className : mandatoryClassLoad)
                Class.forName(className, true, launchClassLoader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        launchClassLoader.registerTransformer("io.ottermc.fml189.OMCTransformer");
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
