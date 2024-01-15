package io.github.ottermc.modules.hypixel;

import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.screen.render.Icon;

public class LevelHead extends Module {

	private static final Icon ICON = Icon.getIconIgnoreException("module/ideahead_icon.png");
	
	public LevelHead() {
		super("Level Head", Category.HYPIXEL);
		setActive(true);
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
}
