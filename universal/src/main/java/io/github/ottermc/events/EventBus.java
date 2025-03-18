package io.github.ottermc.events;

import io.github.ottermc.events.listeners.RunTickListener;

import java.util.ArrayList;
import java.util.List;

public class EventBus {

	private static final List<Listener> listeners = new ArrayList<>();
	
	public static void add(Listener listener) {
		listeners.add(listener);
	}
	
	public static void remove(Listener listener) {
		listeners.remove(listener);
	}
	
	public static void fire(Event event) {
		event.fire(listeners);
	}
}
