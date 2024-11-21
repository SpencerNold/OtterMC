package io.github.ottermc.pvp;

import agent.transformation.ClassAdapter;
import io.github.ottermc.Client;
import io.github.ottermc.ClientLogger;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;
import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.pvp.modules.analytical.Analytical;
import io.github.ottermc.pvp.modules.hud.*;
import io.github.ottermc.pvp.modules.hypixel.AutoGG;
import io.github.ottermc.pvp.modules.hypixel.GameMacro;
import io.github.ottermc.pvp.modules.utility.Fullbright;
import io.github.ottermc.pvp.modules.utility.Zoom;
import io.github.ottermc.pvp.modules.visual.*;
import io.github.ottermc.pvp.screen.hud.ClientDisplay;
import io.github.ottermc.pvp.transformers.*;
import io.github.ottermc.screen.hud.HudManager;
import io.github.ottermc.screen.impl.MenuScreen;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.lang.instrument.UnmodifiableClassException;

@Plugin(name = "OtterMC 1.8.9 Pvp", version = "ALPHA-v1.0.0 (1.8.9)", target = "1.8.9")
public class Main implements Implementation {

    @Override
    public void onPreInit(ClassAdapter adapter) {
        adapter.register(EntityPlayerSPTransformer.class);
        adapter.register(EntityTransformer.class);
        adapter.register(GuiScreenTransformer.class);
        adapter.register(ItemRendererTransformer.class);
        adapter.register(LayerArmorBaseTransformer.class);
        adapter.register(PlayerControllerMPTransformer.class);
        adapter.register(RenderEntityItemTransformer.class);
        adapter.register(RenderGlobalTransformer.class);
        adapter.register(RenderItemTransformer.class);
    }

    @Override
    public void onEnable() {
        registerKeybinds();
    }

    @Override
    public void onPostInit() {
        ClassAdapter adapter = ClassAdapter.getInstance();
        adapter.register(RendererLivingEntityTransformer.class);
        try {
            adapter.execute();
        } catch (UnmodifiableClassException e) {
            ClientLogger.display(e);
        }
        registerModules();
        registerHuds();
    }

    private void registerModules() {
        ModuleManager manager = Client.getModManager();
        // HUD
        manager.register(new ArmorStatus());
        manager.register(new Array());
        manager.register(new ClickCounter());
        manager.register(new Coordinate());
        manager.register(new Direction());
        manager.register(new GuiBlur());
        manager.register(new KeyStroke());
        manager.register(new PotionEffect());

        // Utility
//		modManager.register(new Chat()); TODO Remove instance null check in this class when finished
        manager.register(new Fullbright());
        manager.register(new Zoom());

        // Visual
        manager.register(new BlockOutline());
        manager.register(new ColorTheme());
        manager.register(new DamageColor());
        manager.register(new EnchantmentGlint());
        manager.register(new LargeItems());
        manager.register(new OldAnimation());

        // Hypixel
        manager.register(new AutoGG());
        manager.register(new GameMacro());

        // Analytical
        manager.register(new Analytical());
    }

    private void registerKeybinds() {
        KeybindManager manager = Client.getKeyManager();
        manager.register(Keyboard.KEY_RSHIFT, () -> {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen == null)
                mc.displayGuiScreen(new MenuScreen());
        });
        manager.register(Keyboard.KEY_M, () -> {
            if (GameMacro.isModActive()) {
                System.out.println("Not implemented yet!");
            }
        });
    }

    void registerHuds() {
        HudManager manager = Client.getHudManager();
        // Client HUD
        manager.register(ClientDisplay.ARMOR_STATUS);
        manager.register(ClientDisplay.ARRAY);
        manager.register(ClientDisplay.CLICK_COUNTER);
        manager.register(ClientDisplay.COORDINATE);
        manager.register(ClientDisplay.DIRECTION);
        manager.register(ClientDisplay.KEYSTROKE);
        manager.register(ClientDisplay.POTION_EFFECT);
    }

    @Override
    public void onDisable() {

    }
}
