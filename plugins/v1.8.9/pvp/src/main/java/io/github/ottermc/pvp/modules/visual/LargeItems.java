package io.github.ottermc.pvp.modules.visual;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.modules.Module;
import io.github.ottermc.pvp.listeners.GetItemScaleListener;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Arrays;
import java.util.List;

public class LargeItems extends Module implements GetItemScaleListener {

	private static final Icon ICON = Icon.getIconIgnoreException("module/size_icon.png");
	
	// This is what is probably wanted for UHC, but I mean, who plays that anymore? *make this customizable*
	private final List<Item> items = Arrays.asList(Items.gold_nugget, Items.gold_ingot, Items.golden_apple, Items.skull);
	
	public LargeItems() {
		super("Large Items", CategoryList.VISUAL);
	}
	
	@Override
	public void onEnable() {
		EventBus.add(this);
	}
	
	@Override
	public void onDisable() {
		EventBus.remove(this);
	}
	
	@Override
	public void onGetItemScale(GetItemScaleEvent event) {
		EntityItem entity = (EntityItem) event.getEntityItemParameter();
		if (items.contains(entity.getEntityItem().getItem()))
			event.setScale(event.getX() * 2.5f, event.getY() * 2.5f, event.getZ() * 2.5f);
	}

	@Override
	public Icon getIcon() {
		return ICON;
	}
}
