package io.github.ottermc.smp;

import agent.ClassTransformer;
import io.github.ottermc.Client;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;
import io.github.ottermc.modules.CategoryRegistry;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.smp.transformers.GameRendererTransformer;

@Plugin(name = "OtterMC SMP", version = Client.VERSION, target = "latest")
public class Main implements Implementation {

    @Override
    public void onPreInit(ClassTransformer transformer) {
        CategoryRegistry.register(CategoryList.values());

        transformer.register(GameRendererTransformer.class);
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
        ModuleManager manager = Client.getInstance().getModuleManager();
        // Game
        // Movement
        // World
    }
}
