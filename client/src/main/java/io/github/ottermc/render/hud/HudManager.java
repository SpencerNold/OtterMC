package io.github.ottermc.render.hud;

import io.github.ottermc.events.listeners.RenderGameOverlayListener;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public abstract class HudManager implements RenderGameOverlayListener {

    private final LinkedList<Component> components = new LinkedList<>();

    public void register(Component component) {
        components.add(component);
    }

    public LinkedList<Component> getComponents() {
        return components;
    }

    public Collection<MovableComponent> getMovableComponents() {
        return components.stream().filter(c -> c instanceof MovableComponent).map(c -> (MovableComponent) c).collect(Collectors.toList());
    }

    @Override
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        for (Component c : components) {
            if (!c.isVisible())
                continue;
            if (c instanceof MovableComponent) {
                ((MovableComponent) c).enableTranslate(event.getContext());
                drawComponent(event.getContext(), c);
                ((MovableComponent) c).disableTranslate(event.getContext());
            } else
                c.draw(event.getContext());
        }
    }

    protected abstract void drawComponent(Object context, Component component);
}
