package io.github.ottermc.pvp.modules.hud;

import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.BooleanSetting;
import io.github.ottermc.modules.setting.ColorSetting;
import io.github.ottermc.pvp.modules.visual.ColorTheme;
import io.github.ottermc.render.Color;
import io.github.ottermc.pvp.screen.hud.ClientDisplay;
import io.github.ottermc.screen.render.Icon;

public class PotionEffect extends Module {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/potion_icon.png");
	
	private static PotionEffect instance;
	
	private final ColorSetting color = new ColorSetting("Color", new Color(-1), false);
	private final BooleanSetting theme = new BooleanSetting("Use Theme", true);

	public PotionEffect() {
		super("Potion Effects", Category.HUD);
		instance = this;
	}

	@Override
	public void onEnable() {
		ClientDisplay.POTION_EFFECT.setVisible(true);
	}
	
	@Override
	public void onDisable() {
		ClientDisplay.POTION_EFFECT.setVisible(false);
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	@Override
	public Storable<?>[] getWritables() {
		return new Storable<?>[] { color, theme };
	}
	
	public static Color getColor() {
		return (instance.theme.getValue() && ColorTheme.isModActive()) ? ColorTheme.getColorTheme() : instance.color.getValue();
	}
}
