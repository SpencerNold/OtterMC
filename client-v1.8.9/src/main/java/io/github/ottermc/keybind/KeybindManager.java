package io.github.ottermc.keybind;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;

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
			if (Keyboard.isKeyDown(keybind.getCode()) && !keybind.isDown()) {
				keybind.setDown(true);
				keybind.getRunnable().run();
			}
			if (!Keyboard.isKeyDown(keybind.getCode()) && keybind.isDown())
				keybind.setDown(false);
		}
	}
}
