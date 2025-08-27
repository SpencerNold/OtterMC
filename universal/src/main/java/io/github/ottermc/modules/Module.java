package io.github.ottermc.modules;

import io.github.ottermc.io.ByteBuf;

public abstract class Module implements Renderable {
	
	protected final String name;
	protected final Category category;
	private boolean active;
	
	public Module(String name, Category category) {
		this.name = name;
		this.category = category;
		this.active = false;
	}
	
	public void onEnable() {
	}
	
	public void onDisable() {
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
		if (active) onEnable();
		else onDisable();
	}
	
	public void toggle() {
		setActive(!active);
	}
	
	public String getName() {
		return name;
	}
	
	public Category getCategory() {
		return category;
	}

	public Setting<?> getSettingByName(String name) {
		Setting<?>[] settings = getSettings();
		for (Setting<?> setting : settings) {
			if (setting.name.equals(name))
				return setting;
		}
		return null;
	}
	
	public Writable<ByteBuf>[] getWritables() {
		return null;
	}

	public Setting<?>[] getSettings() {
		Writable<ByteBuf>[] writables = getWritables();
		if (writables == null)
			return new Setting<?>[0];
		Setting<?>[] settings = new Setting[writables.length];
		for (int i = 0; i < writables.length; i++) {
			if (writables[i] instanceof Setting<?>)
				settings[i] = (Setting<?>) writables[i];
		}
		int nullCount = 0;
		for (Setting<?> setting : settings) {
			if (setting == null)
				nullCount++;
		}
		if (nullCount != 0) {
			Setting<?>[] copy = new Setting[settings.length - nullCount];
			int index = 0;
			for (Setting<?> setting : settings) {
				if (setting != null) {
					copy[index] = setting;
					index++;
				}
			}
			settings = copy;
		}
		return settings;
	}

	public String getDescription() {
		return null;
	}
	
	public int getSerialId() {
		return (category.getDisplayName() + name).hashCode();
	}

	public boolean shouldRenderInMenu() {
		return true;
	}
}
