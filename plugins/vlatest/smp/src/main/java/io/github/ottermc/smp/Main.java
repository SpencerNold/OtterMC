package io.github.ottermc.smp;

import agent.transformation.ClassAdapter;
import io.github.ottermc.Client;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.smp.modules.world.LightLevel;
import io.github.ottermc.smp.transformers.GameRendererTransformer;

@Plugin(name = "OtterMC 1.21.3 SMP", version = Client.VERSION, target = "1.21.3")
public class Main implements Implementation {

    @Override
    public void onPreInit(ClassAdapter adapter) {
        adapter.register(GameRendererTransformer.class);
    }

    @Override
    public void onEnable() {
        
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onPostInit() {
        registerModules();
    }

    private void registerModules() {
        ModuleManager manager = Client.getModManager();
        // Game
        // Movement
        // World
        manager.register(new LightLevel());
    }
}
