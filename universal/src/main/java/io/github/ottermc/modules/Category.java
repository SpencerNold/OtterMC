package io.github.ottermc.modules;

public enum Category {
	
	VISUAL("Visual"), HUD("Display"), UTILITY("Game"), WORLD("World"), ONLINE("Online"), ANALYTICAL("Analytical");
	
	private final String displayName;
	
	Category(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
