package io.github.ottermc.pvp;

import agent.transformation.ClassAdapter;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;

@Plugin(name = "OtterMC 1.8.9 Pvp", version = "ALPHA-v1.0.0 (1.8.9)")
public class Main implements Implementation {

    @Override
    public void onPreInit(ClassAdapter adapter) {
        System.out.println("Pre-Init");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
