package io.github.ottermc.smp;

import io.github.ottermc.Client;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;
import io.github.ottermc.modules.CategoryRegistry;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.smp.modules.world.BiomeFinder;
import io.github.ottermc.smp.transformers.GameRendererTransformer;
import io.ottermc.transformer.TransformerRegistry;

@Plugin(name = "OtterMC SMP", version = Client.VERSION, target = "latest")
public class Main implements Implementation {

    @Override
    public void onPreInit(TransformerRegistry registry) {
        CategoryRegistry.register(CategoryList.values());

        registry.register(GameRendererTransformer.class);
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
        manager.register(new BiomeFinder());
    }
}
