package io.github.ottermc;

import agent.Agent;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.PostInitializeListener;
import io.github.ottermc.listeners.RunTickListener;
import io.github.ottermc.screen.hud.GameDisplay;
import io.github.ottermc.screen.hud.HudManager;
import io.github.ottermc.screen.impl.MainMenuScreen;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.nio.ByteBuffer;

public class InitializationManager implements PostInitializeListener, RunTickListener {

    private boolean hasPostInitialized;
    private int timer;

    public InitializationManager() {
        this.hasPostInitialized = false;
        this.timer = 20;
    }

    @Override
    public void onPostInitializeListener(PostInitializeEvent event) {
        attemptPostInitializeProcess();
    }

    @Override
    public void onRunTick(RunTickEvent event) {
        if (hasPostInitialized) {
            EventBus.remove(this);
            return;
        }
        if (timer <= 0) {
            attemptPostInitializeProcess();
            timer = 20;
            return;
        }
        timer--;
    }

    private void attemptPostInitializeProcess() {
        Display.setTitle(Client.NAME + " " + Client.VERSION);
        Display.setIcon(new ByteBuffer[] { Icon.readIconToBuffer("otter_icon_16x16.png"), Icon.readIconToBuffer("otter_icon_32x32.png"), });
        registerGameHuds();
        Agent.PLUGINS.forEach(((plugin, implementation) -> {
            implementation.onPostInit();
        }));
        Client.getClientStorage().init();
        try {
            Client.getClientStorage().read();
        } catch (IOException e) {
            ClientLogger.display(e);
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null && mc.currentScreen != null && mc.currentScreen.getClass() == GuiMainMenu.class)
            mc.displayGuiScreen(new MainMenuScreen());
        Client.getErrorManager().postInit();
        hasPostInitialized = true;
    }

    private void registerGameHuds() {
        HudManager manager = Client.getHudManager();
        // Default Minecraft HUD
        manager.register(GameDisplay.PUMPKIN_OVERLAY);
        manager.register(GameDisplay.NAUSEA_EFFECT);
        manager.register(GameDisplay.TOOLTIP);
        manager.register(GameDisplay.BOSS_BAR);
        manager.register(GameDisplay.PLAYER_STATS);
        manager.register(GameDisplay.SLEEP_MENU);
        manager.register(GameDisplay.EXP_BAR);
        manager.register(GameDisplay.OVERLAY_TEXT);
        manager.register(GameDisplay.SCOREBOARD);
        manager.register(GameDisplay.TITLE);
        manager.register(GameDisplay.TAB);
    }
}
