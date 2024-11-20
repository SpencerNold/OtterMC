package io.github.ottermc.pvp.screen.hud;

import io.github.ottermc.pvp.screen.hud.minecraft.*;
import io.github.ottermc.screen.hud.Component;

public final class GameDisplay {

	public static final Component BOSS_BAR = new MinecraftBossBarHud();
	public static final Component EXP_BAR = new MinecraftExperienceBarHud();
	public static final Component NAUSEA_EFFECT = new MinecraftNauseaEffectHud();
	public static final Component OVERLAY_TEXT = new MinecraftOverlayTextHud();
	public static final Component PLAYER_STATS = new MinecraftPlayerStatsHud();
	public static final Component PUMPKIN_OVERLAY = new MinecraftPumpkinOverlayHud();
	public static final Component SCOREBOARD = new MinecraftScoreboardHud();
	public static final Component SLEEP_MENU = new MinecraftSleepMenuHud();
	public static final Component TAB = new MinecraftTabHud();
	public static final Component TITLE = new MinecraftTitleHud();
	public static final Component TOOLTIP = new MinecraftTooltipHud();
	
}
