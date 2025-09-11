package io.github.ottermc.pvp.modules.hud;

import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.BooleanSetting;
import io.github.ottermc.modules.setting.ColorSetting;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.pvp.screen.hud.ClientDisplay;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.render.Icon;

public class Direction extends Module {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/compass_icon.png");
	
	private static Direction instance;
	
	private final ColorSetting color = new ColorSetting("Color", new Color(-1), false);
	private final BooleanSetting ttf = new BooleanSetting("TrueType Font", true);

	public Direction() {
		super("Directions", CategoryList.DISPLAY);
		instance = this;
	}
	
	@Override
	public void onEnable() {
		ClientDisplay.DIRECTION.setVisible(true);
	}
	
	@Override
	public void onDisable() {
		ClientDisplay.DIRECTION.setVisible(false);
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	@Override
	public Storable<?>[] getWritables() {
		return new Storable<?>[] { color, ttf };
	}
	
	public static Color getColor() {
		return instance.color.getValue();
	}
	
	public static boolean shouldUseClientFont() {
		return instance.ttf.getValue();
	}
}
