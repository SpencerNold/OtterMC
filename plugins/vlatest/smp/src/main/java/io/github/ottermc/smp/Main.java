package io.github.ottermc.smp;

import agent.transformation.ClassAdapter;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;

@Plugin(name = "OtterMC 1.21.3 SMP", version = "ALPHA-v1.0.0 (1.21.3)", target = "1.21.3")
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
