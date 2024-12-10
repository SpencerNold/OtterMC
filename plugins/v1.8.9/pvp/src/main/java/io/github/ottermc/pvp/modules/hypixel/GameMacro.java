package io.github.ottermc.pvp.modules.hypixel;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.screen.render.Icon;

public class GameMacro extends HypixelModule implements RunTickListener {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/keyboard_icon.png");
	
	private static GameMacro instance;
	
	public GameMacro() { // hard code the commands, and add a 3 second delay timer
		super("Macros");
		instance = this;
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
	public void onRunTick(RunTickEvent event) {
		if (!isConnectedToHypixel())
			return;
		
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	public static boolean isModActive() {
		return instance.isActive();
	}
}
