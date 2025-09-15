package io.github.ottermc.pvp;

import agent.ClassTransformer;
import io.github.ottermc.Client;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;
import io.github.ottermc.logging.Logger;
import io.github.ottermc.modules.CategoryRegistry;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.pvp.modules.hud.*;
import io.github.ottermc.pvp.modules.hypixel.AutoGG;
import io.github.ottermc.pvp.modules.hypixel.GameMacro;
import io.github.ottermc.pvp.modules.utility.Fullbright;
import io.github.ottermc.pvp.modules.utility.Zoom;
import io.github.ottermc.pvp.modules.visual.*;
import io.github.ottermc.pvp.screen.hud.ClientDisplay;
import io.github.ottermc.pvp.transformers.*;
import io.github.ottermc.screen.hud.HudManager;

import java.lang.instrument.UnmodifiableClassException;

@Plugin(name = "OtterMC Pvp", version = Client.VERSION, target = "1.8.9")
public class Main implements Implementation {

    @Override
    public void onPreInit(ClassTransformer transformer) {
        CategoryRegistry.register(CategoryList.values());

        transformer.register(EntityPlayerSPTransformer.class);
        transformer.register(EntityTransformer.class);
        transformer.register(GuiScreenTransformer.class);
        transformer.register(ItemRendererTransformer.class);
        transformer.register(LayerArmorBaseTransformer.class);
        transformer.register(MinecraftTransformer.class);
        transformer.register(PlayerControllerMPTransformer.class);
        transformer.register(RenderEntityItemTransformer.class);
        transformer.register(RenderGlobalTransformer.class);
        transformer.register(RenderItemTransformer.class);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onPostInit() {
        ClassTransformer adapter = ClassTransformer.getInstance();
        adapter.clear();
        adapter.register(RendererLivingEntityTransformer.class);
        try {
            adapter.execute();
        } catch (UnmodifiableClassException e) {
            Logger.error(e);
        }
        registerHuds();
        registerModules();
    }

    private void registerModules() {
        ModuleManager manager = Client.getInstance().getModuleManager();
        // HUD
        manager.register(new ArmorStatus());
        manager.register(new ClickCounter());
        manager.register(new Coordinate());
        manager.register(new GuiBlur());
        manager.register(new KeyStroke());
        manager.register(new PotionEffect());

        // Utility
        manager.register(new Fullbright());
        manager.register(new Zoom());

        // Visual
        manager.register(new BlockOutline());
        manager.register(new DamageColor());
        manager.register(new EnchantmentGlint());
        manager.register(new LargeItems());
        manager.register(new OldAnimation());

        // Hypixel
        manager.register(new AutoGG());
        manager.register(new GameMacro());
    }

    void registerHuds() {
        HudManager manager = Client.getInstance().getHudManager();
        // Client HUD
        manager.register(ClientDisplay.ARMOR_STATUS);
        manager.register(ClientDisplay.CLICK_COUNTER);
        manager.register(ClientDisplay.COORDINATE);
        manager.register(ClientDisplay.KEYSTROKE);
        manager.register(ClientDisplay.POTION_EFFECT);
    }

    @Override
    public void onDisable() {

    }
}
