package io.github.ottermc.smp;

import io.github.ottermc.Client;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.smp.modules.game.FishingHelper;
import io.github.ottermc.smp.modules.game.TradeSelector;
import io.github.ottermc.smp.transformers.GameRendererTransformer;

@Plugin(name = "OtterMC 1.21.4 SMP", version = Client.VERSION, target = "1.21.4")
public class Main implements Implementation {

    @Override
    public void onPreInit(ClassAdapter1 adapter) {
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
        manager.register(new FishingHelper());
        // Movement
        // World
        manager.register(new TradeSelector());
    }
}
