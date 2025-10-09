package io.github.ottermc.events;

import java.util.List;

public abstract class Event {
	public abstract void fire(List<Listener> listeners);
}
