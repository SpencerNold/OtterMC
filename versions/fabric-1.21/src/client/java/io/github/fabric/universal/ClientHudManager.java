package io.github.fabric.universal;

import io.github.ottermc.render.hud.Component;
import io.github.ottermc.render.hud.HudManager;

public class ClientHudManager extends HudManager {
    @Override
    protected void drawComponent(Object context, Component component) {
        component.draw(context);
    }
}
