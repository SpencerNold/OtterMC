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

public class Coordinate extends Module {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/map_icon.png");
	
	private static Coordinate instance;
	
	private final ColorSetting color = new ColorSetting("Color", new Color(-1), false);
	private final BooleanSetting theme = new BooleanSetting("Use Theme", true);
	private final BooleanSetting ttf = new BooleanSetting("TrueType Font", false);

	public Coordinate() {
		super("Coordinates", Category.HUD);
		instance = this;
	}
	
	@Override
	public void onEnable() {
		ClientDisplay.COORDINATE.setVisible(true);
	}
	
	@Override
	public void onDisable() {
		ClientDisplay.COORDINATE.setVisible(false);
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	@Override
	public Storable<?>[] getWritables() {
		return new Storable<?>[] { color, theme, ttf };
	}
	
	public static Color getColor() {
		return (instance.theme.getValue() && ColorTheme.isModActive()) ? ColorTheme.getColorTheme() : instance.color.getValue();
	}
	
	public static boolean shouldUseClientFont() {
		return instance.ttf.getValue();
	}
}
