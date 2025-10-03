package io.ottermc.fml189;

import io.ottermc.common.*;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class OMCTweaker implements ITweaker {
    @Override
    public void acceptOptions(List<String> list, File file, File file1, String s) {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
        ((List<String>) Launch.blackboard.get("TweakClasses")).add("io.ottermc.fml189.LateInitTweaker");
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
