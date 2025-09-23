package io.github.ottermc.smp.modules.world;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.listeners.DrawOverlayListener;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.BooleanSetting;
import io.github.ottermc.modules.setting.KeyboardSetting;
import io.github.ottermc.modules.setting.StringSetting;
import io.github.ottermc.smp.CategoryList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class BiomeFinder extends Module implements RunTickListener, DrawOverlayListener {

    private final StringSetting seedSetting = new StringSetting("Seed", "0", 1);
    private final BooleanSetting largeBiomesSetting = new BooleanSetting("Large Biomes", false);
    private final KeyboardSetting mapKeyboardSetting = new KeyboardSetting("Map Keybind", GLFW.GLFW_KEY_B);

    public BiomeFinder() {
        super("Biome Finder", CategoryList.WORLD);
    }

    @Override
    public void onEnable() {
        EventBus.add(this);
    }

    @Override
    public void onDisable() {
        EventBus.remove(this);
    }

    @Override
    public void onRunTick(RunTickEvent event) {

    }

    @Override
    public void onDrawOverlay(DrawOverlayEvent event) {
        MinecraftClient client = MinecraftClient.getInstance();
        DrawContext context = event.getContext();
    }

    @Override
    public Storable<?>[] getWritables() {
        return new Storable<?>[] { seedSetting, largeBiomesSetting, mapKeyboardSetting };
    }
}
