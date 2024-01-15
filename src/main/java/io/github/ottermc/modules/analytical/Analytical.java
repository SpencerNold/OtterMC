package io.github.ottermc.modules.analytical;

import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.screen.render.Icon;

public class Analytical extends Module {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/clipboard_icon.png");

	private static Analytical instance;
	
	public Analytical() {
		super("Analytical Data", Category.ANALYTICAL);
		instance = this;
		setActive(true);
	}

	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	public static boolean isModActive() {
		return instance.isActive();
	}
}
