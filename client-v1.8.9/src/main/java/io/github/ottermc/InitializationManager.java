package io.github.ottermc;

import agent.transformation.ClassAdapter;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.PostInitializeListener;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.screen.impl.MainMenuScreen;
import io.github.ottermc.screen.render.Icon;
import io.github.ottermc.transformers.RendererLivingEntityTransformer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.lang.instrument.UnmodifiableClassException;
import java.nio.ByteBuffer;

public class InitializationManager implements PostInitializeListener, RunTickListener {

    private final Client client;
    private boolean hasPostInitialized;
    private int timer;

    public InitializationManager(Client client) {
        this.client = client;
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
        client.registerModules();
        client.registerHuds();
        Client.getClientStorage().init();
        try {
            Client.getClientStorage().read();
        } catch (IOException e) {
            ClientLogger.display(e);
        }
        // Any classes whose static initializers use OpenGL
        ClassAdapter adapter = ClassAdapter.getInstance();
        adapter.register(RendererLivingEntityTransformer.class);
        try {
            adapter.execute();
        } catch (UnmodifiableClassException e) {
            ClientLogger.display(e);
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null && mc.currentScreen != null && mc.currentScreen.getClass() == GuiMainMenu.class)
            mc.displayGuiScreen(new MainMenuScreen());
        Client.getErrorManager().postInit();
        hasPostInitialized = true;
    }
}
