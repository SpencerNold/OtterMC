package io.github.ottermc.modules.visual;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.SetEntityDamageBrightnessListener;
import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.settings.Storable;
import io.github.ottermc.modules.settings.setting.BooleanSetting;
import io.github.ottermc.modules.settings.setting.ColorSetting;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.render.Icon;

public class DamageColor extends Module implements SetEntityDamageBrightnessListener {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/hurt_icon.png");

	private final ColorSetting color = new ColorSetting("Color", Color.DEFAULT, false);
	private final BooleanSetting theme = new BooleanSetting("Use Theme", true);
	
	public DamageColor() {
		super("Damage Color", Category.VISUAL);
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
		Color color = (theme.getValue() && ColorTheme.isModActive()) ? ColorTheme.getColorTheme() : this.color.getValue();
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
		return new Storable<?>[] { color, theme };
	}
}
