package io.github.ottermc.pvp.modules.utility;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.settings.Storable;
import io.github.ottermc.modules.Writable;
import io.github.ottermc.modules.settings.setting.BooleanSetting;
import io.github.ottermc.modules.settings.setting.ColorSetting;
import io.github.ottermc.pvp.modules.visual.ColorTheme;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.render.Icon;

public class Chat extends Module {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/chat_icon.png");
	
	private static Chat instance;

	private final ColorSetting color = new ColorSetting("Color", Color.DEFAULT, false);
	private final BooleanSetting theme = new BooleanSetting("Use Theme", true);
	private final BooleanSetting ttf = new BooleanSetting("TrueType Font", false);
	
	public Chat() {
		super("Chat", Category.UTILITY);
		instance = this;
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	@Override
	public Writable<ByteBuf>[] getWritables() {
		return new Storable<?>[] { color, theme };
	}
	
	public static boolean isModActive() {
		return instance != null && instance.isActive();
	}
	
	public static boolean shouldUseClientFont() {
		return instance.ttf.getValue();
	}
	
	public static Color getColor() {
		return (instance.theme.getValue() && ColorTheme.isModActive()) ? ColorTheme.getColorTheme() : instance.color.getValue();
	}
}
