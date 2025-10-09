package io.github.ottermc.hud;

import io.github.ottermc.hud.client.*;
import io.github.ottermc.screen.hud.Component;

public final class ClientDisplay {

	public static final Component ARMOR_STATUS = new ClientArmorStatusHud();
	public static final Component CLICK_COUNTER = new ClientClickCounterHud();
	public static final Component COORDINATE = new ClientCoordinateHud();
	public static final Component KEYSTROKE = new ClientKeyStrokeHud();
	public static final Component POTION_EFFECT = new ClientPotionEffectHud();
	
}
