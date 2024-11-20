package io.github.ottermc.pvp.screen.hud;

import io.github.ottermc.pvp.screen.hud.client.ClientArmorStatusHud;
import io.github.ottermc.pvp.screen.hud.client.ClientArrayHud;
import io.github.ottermc.pvp.screen.hud.client.ClientClickCounterHud;
import io.github.ottermc.pvp.screen.hud.client.ClientCoordinateHud;
import io.github.ottermc.pvp.screen.hud.client.ClientDirectionHud;
import io.github.ottermc.pvp.screen.hud.client.ClientKeyStrokeHud;
import io.github.ottermc.pvp.screen.hud.client.ClientPotionEffectHud;
import io.github.ottermc.screen.hud.Component;

public final class ClientDisplay {

	public static final Component ARMOR_STATUS = new ClientArmorStatusHud();
	public static final Component ARRAY = new ClientArrayHud();
	public static final Component CLICK_COUNTER = new ClientClickCounterHud();
	public static final Component COORDINATE = new ClientCoordinateHud();
	public static final Component DIRECTION = new ClientDirectionHud();
	public static final Component KEYSTROKE = new ClientKeyStrokeHud();
	public static final Component POTION_EFFECT = new ClientPotionEffectHud();
	
}
