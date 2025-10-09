package io.github.ottermc.keybind;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;

import java.util.ArrayList;
import java.util.List;

public class KeybindManager implements RunTickListener {
	
	private final List<Keybind> keybinds = new ArrayList<>();
	
	public KeybindManager() {
		EventBus.add(this);
	}
	
	public void register(int code, Runnable runnable) {
		keybinds.add(new Keybind(code, runnable));
	}
	
	@Override
	public void onRunTick(RunTickEvent event) {
		for (Keybind keybind : keybinds) {
			if (UniversalKeyboard.isKeyDown(keybind.getCode()) && !keybind.isDown()) {
				keybind.setDown(true);
				keybind.getRunnable().run();
			}
			if (!UniversalKeyboard.isKeyDown(keybind.getCode()) && keybind.isDown())
				keybind.setDown(false);
		}
	}
}
