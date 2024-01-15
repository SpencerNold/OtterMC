package io.github.ottermc.modules.hypixel;

import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.screen.render.Icon;

public class TitleHead extends Module {

	private static final Icon ICON = Icon.getIconIgnoreException("module/title_icon.png");
	
	public TitleHead() {
		super("Title Head", Category.HYPIXEL);
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
}
