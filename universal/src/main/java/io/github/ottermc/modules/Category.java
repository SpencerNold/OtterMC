package io.github.ottermc.modules;

public class Category {

	private final String displayName;
	
	public Category(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}

	@Override
	public int hashCode() {
		return displayName.hashCode();
	}
}
