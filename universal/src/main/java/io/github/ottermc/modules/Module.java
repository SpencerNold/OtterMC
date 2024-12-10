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
	
	public Writable<ByteBuf>[] getWritables() {
		return null;
	}
	
	public String getDescription() {
		return null;
	}
	
	public int getSerialId() {
		return (category.name() + name).hashCode();
	}
}
