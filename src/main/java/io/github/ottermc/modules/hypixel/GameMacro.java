package io.github.ottermc.modules.hypixel;

import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.screen.render.Icon;

public class GameMacro extends Module {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/keyboard_icon.png");

	public GameMacro() {
		super("Macros", Category.HYPIXEL);
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
}
