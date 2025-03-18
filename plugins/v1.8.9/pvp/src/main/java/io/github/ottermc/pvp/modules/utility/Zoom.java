package io.github.ottermc.pvp.modules.utility;

import org.lwjgl.input.Keyboard;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.KeyboardSetting;
import io.github.ottermc.modules.storable.FloatStorage;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class Zoom extends Module implements RunTickListener {

	private static final Icon ICON = Icon.getIconIgnoreException("module/zoom_icon.png");
	
	private final KeyboardSetting setting = new KeyboardSetting("Zoom Key", Keyboard.KEY_C);
	private final FloatStorage fieldOfView = new FloatStorage(70.0f);
	
	private boolean zooming;
	
	public Zoom() {
		super("Zoom", Category.GAME);
		setActive(true);
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
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.currentScreen != null)
			return;
		GameSettings settings = mc.gameSettings;
		boolean down = Keyboard.isKeyDown(setting.getValue());
		if (down && !zooming) {
			zooming = true;
			settings.smoothCamera = true;
			fieldOfView.setValue(settings.fovSetting);
			settings.fovSetting = 19.0f;
		}
		if (!down && zooming) {
			zooming = false;
			settings.smoothCamera = false;
			settings.fovSetting = fieldOfView.getValue();
		}
	}
	
	@Override
	public Storable<?>[] getWritables() {
		return new Storable<?>[] { setting, fieldOfView };
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
}
