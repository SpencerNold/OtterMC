package io.github.ottermc.modules.hypixel;

import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.screen.render.Icon;

public class AutoTip extends Module {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/tip_icon.png");

	public AutoTip() {
		super("AutoTip", Category.HYPIXEL);
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
}
