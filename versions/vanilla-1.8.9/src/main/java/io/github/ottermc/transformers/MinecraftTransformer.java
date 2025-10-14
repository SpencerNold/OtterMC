package io.github.ottermc.transformers;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.PostInitializeListener.PostInitializeEvent;
import io.github.ottermc.events.listeners.RunTickListener.RunTickEvent;
import io.github.ottermc.events.listeners.ClickMouseListener;
import io.github.ottermc.events.listeners.RightClickMouseListener;
import io.github.ottermc.listeners.SaveGameListener.SaveGameEvent;
import io.github.ottermc.listeners.UpdateDisplayListener.UpdateDisplayEvent;
import io.github.ottermc.screen.impl.MainMenuScreen;
import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

@Transformer(className = "net/minecraft/client/Minecraft")
public class MinecraftTransformer {

    @Injector(target = Target.HEAD, name = "runTick()V")
    public void onTick(Minecraft mc, Callback callback) {
        RunTickEvent event = new RunTickEvent(mc);
        EventBus.fire(event);
    }

    @Injector(target = Target.HEAD, name = "updateDisplay()V")
    public void onUpdateDisplay(Minecraft mc, Callback callback) {
        UpdateDisplayEvent event = new UpdateDisplayEvent();
        EventBus.fire(event);
    }

    @Injector(target = Target.TAIL, name = "startGame()V")
    public void onStartGame(Minecraft mc, Callback callback) {
        PostInitializeEvent event = new PostInitializeEvent();
        EventBus.fire(event);
    }

    @Injector(target = Target.HEAD, name = "displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V")
    public void onDisplayGuiScreen(Minecraft mc, GuiScreen gui, Callback callback) {
        // Replaces screens with wrapped classes of screens
        if ((gui == null && mc.theWorld == null) || (gui != null && gui.getClass() == GuiMainMenu.class)) {
            mc.displayGuiScreen(new MainMenuScreen());
            callback.setCanceled(true);
        }
    }

    @Injector(target = Target.HEAD, name = "displayInGameMenu()V")
    public void onDisplayInGameMenu(Minecraft mc, Callback callback) {
        if (mc.currentScreen == null) {
            SaveGameEvent event = new SaveGameEvent();
            EventBus.fire(event);
        }
    }

    @Injector(target = Target.HEAD, name = "clickMouse()V")
    public void onClickMouse(Minecraft mc, Callback callback) {
        ClickMouseListener.ClickMouseEvent event = new ClickMouseListener.ClickMouseEvent();
        EventBus.fire(event);
    }

    @Injector(target = Target.HEAD, name = "rightClickMouse()V")
    public void onRightClickMouse(Minecraft mc, Callback callback) {
        RightClickMouseListener.RightClickMouseEvent event = new RightClickMouseListener.RightClickMouseEvent();
        EventBus.fire(event);
    }
}
