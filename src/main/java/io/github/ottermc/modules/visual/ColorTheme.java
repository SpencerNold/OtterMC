package io.github.ottermc.modules.visual;

import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.settings.Storable;
import io.github.ottermc.modules.settings.setting.EnumSetting;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.util.MathHelper;

public class ColorTheme extends Module {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/paint_icon.png");
	
	private static ColorTheme instance;
	
	private final EnumSetting<Theme> theme = new EnumSetting<>("Theme", Theme.class, Theme.RAINBOW);
	
	public ColorTheme() {
		super("Color Theme", Category.VISUAL);
		instance = this;
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	@Override
	public Storable<?>[] getWritables() {
		return new Storable<?>[] { theme };
	}

	public static Color getColorTheme() {
		Color color = instance.theme.getEnumValue().color;
		if (color == null) {
			float x = System.currentTimeMillis() % 2000 / 1000.0f;
			float pi = (float) Math.PI;
			float r = 0.5F + 0.5F * MathHelper.sin(x * pi);
			float g = 0.5F + 0.5F * MathHelper.sin((x + 4.0f / 3.0f) * pi);
			float b = 0.5F + 0.5F * MathHelper.sin((x + 8.0f / 3.0f) * pi);
			color = new Color(r, g, b, 1.0f);
		}
		return color;
	}
	
	public static boolean isModActive() {
		return instance.isActive();
	}
	
	public enum Theme {
		
		DARK_PURPLE(Color.DEFAULT), AQUA(new Color(0xFF4BADAB)), DARK_AQUA(new Color(0xFF05696B, true)), RAINBOW(null);
		
		private final Color color;
		
		private Theme(Color color) {
			this.color = color;
		}
	}
}
