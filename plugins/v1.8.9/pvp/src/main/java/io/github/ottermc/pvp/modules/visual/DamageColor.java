package io.github.ottermc.pvp.modules.visual;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.ColorSetting;
import io.github.ottermc.pvp.listeners.SetEntityDamageBrightnessListener;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.render.Icon;

public class DamageColor extends Module implements SetEntityDamageBrightnessListener {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/hurt_icon.png");

	private final ColorSetting color = new ColorSetting("Color", Color.getDefault(), false);

	public DamageColor() {
		super("Damage Color", CategoryList.VISUAL);
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
	public void onSetEntityDamageBrightness(SetEntityDamageBrightnessEvent event) {
		Color color = this.color.getValue();
		event.setRed(color.getRedNormal());
		event.setGreen(color.getGreenNormal());
		event.setBlue(color.getBlueNormal());
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	@Override
	public Storable<?>[] getWritables() {
		return new Storable<?>[] { color };
	}
}
