package io.github.ottermc.modules.hud;

import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.settings.Storable;
import io.github.ottermc.modules.settings.setting.BooleanSetting;
import io.github.ottermc.modules.settings.setting.ColorSetting;
import io.github.ottermc.modules.settings.setting.StringSetting;
import io.github.ottermc.modules.visual.ColorTheme;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.hud.ClientDisplay;
import io.github.ottermc.screen.render.Icon;

public class Array extends Module {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/list_icon.png");
	
	private static Array instance;
	
	private final ColorSetting color = new ColorSetting("Color", new Color(-1), false);
	private final BooleanSetting theme = new BooleanSetting("Use Theme", true);
	private final BooleanSetting ttf = new BooleanSetting("TrueType Font", false);
	private final StringSetting text = new StringSetting("Text", "", 6);
	
	public Array() {
		super("Custom Array", Category.HUD);
		instance = this;
	}
	
	@Override
	public void onEnable() {
		ClientDisplay.ARRAY.setVisible(true);
	}
	
	@Override
	public void onDisable() {
		ClientDisplay.ARRAY.setVisible(false);
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	@Override
	public Storable<?>[] getWritables() {
		return new Storable<?>[] { color, theme, ttf, text };
	}
	
	public static Color getColor() {
		return (instance.theme.getValue() && ColorTheme.isModActive()) ? ColorTheme.getColorTheme() : instance.color.getValue();
	}
	
	public static boolean shouldUseClientFont() {
		return instance.ttf.getValue();
	}
	
	public static String getText() {
		return instance.text.getValue();
	}
}
