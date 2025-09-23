package io.github.ottermc.pvp;

import io.github.ottermc.Client;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;
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
import io.ottermc.transformer.TransformerRegistry;

@Plugin(name = "OtterMC Pvp", version = Client.VERSION, target = "1.8.9")
public class Main implements Implementation {

    @Override
    public void onPreInit(TransformerRegistry registry) {
        CategoryRegistry.register(CategoryList.values());

        registry.register(EntityPlayerSPTransformer.class);
        registry.register(EntityTransformer.class);
        registry.register(GuiScreenTransformer.class);
        registry.register(ItemRendererTransformer.class);
        registry.register(LayerArmorBaseTransformer.class);
        registry.register(MinecraftTransformer.class);
        registry.register(PlayerControllerMPTransformer.class);
        registry.register(RenderEntityItemTransformer.class);
        registry.register(RenderGlobalTransformer.class);
        registry.register(RenderItemTransformer.class);

        registry.registerPost(RendererLivingEntityTransformer.class);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onPostInit() {
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
        manager.register(new UIScheme());

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
