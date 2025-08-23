package io.github.ottermc.pvp.modules.utility;

import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.storable.FloatStorage;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class Fullbright extends Module {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/flashlight_icon.png");

	private final FloatStorage gamma = new FloatStorage(-1.0f);
	
	public Fullbright() {
		super("Fullbright", CategoryList.GAME);
	}
	
	@Override
	public void onEnable() {
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		if (gamma.getValue() == -1)
			gamma.setValue(settings.gammaSetting);
		settings.gammaSetting = 16.0f;
	}
	
	@Override
	public void onDisable() {
		Minecraft.getMinecraft().gameSettings.gammaSetting = gamma.getValue() == -1 ? 1 : gamma.getValue();
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	@Override
	public Storable<?>[] getWritables() {
		return new Storable<?>[] { gamma };
	}
	
	@Override
	public String getDescription() {
		return "Increases the game brightness to see as if there were no shadows or darkness.";
	}
}
