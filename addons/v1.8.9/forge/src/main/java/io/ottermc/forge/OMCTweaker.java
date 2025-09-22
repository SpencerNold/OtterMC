package io.ottermc.forge;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.List;

public class OMCTweaker implements ITweaker {

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
        launchClassLoader.registerTransformer("io.ottermc.forge.ForgeTransformer");
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[] { "--tweakClass", "net.minecraftforge.fml.common.launcher.FMLTweaker" };
    }
}
