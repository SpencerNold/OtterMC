package io.github.ottermc.modules;

public enum Category {
	
	VISUAL("Visual"), HUD("Display"), UTILITY("Game"), HYPIXEL("Hypixel"), ANALYTICAL("Analytical");
	
	private final String displayName;
	
	private Category(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
