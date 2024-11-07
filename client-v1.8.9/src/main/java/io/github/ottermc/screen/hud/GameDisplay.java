package io.github.ottermc.screen.hud;

import io.github.ottermc.screen.hud.minecraft.MinecraftBossBarHud;
import io.github.ottermc.screen.hud.minecraft.MinecraftChatHud;
import io.github.ottermc.screen.hud.minecraft.MinecraftExperienceBarHud;
import io.github.ottermc.screen.hud.minecraft.MinecraftNauseaEffectHud;
import io.github.ottermc.screen.hud.minecraft.MinecraftOverlayTextHud;
import io.github.ottermc.screen.hud.minecraft.MinecraftPlayerStatsHud;
import io.github.ottermc.screen.hud.minecraft.MinecraftPumpkinOverlayHud;
import io.github.ottermc.screen.hud.minecraft.MinecraftScoreboardHud;
import io.github.ottermc.screen.hud.minecraft.MinecraftSleepMenuHud;
import io.github.ottermc.screen.hud.minecraft.MinecraftTabHud;
import io.github.ottermc.screen.hud.minecraft.MinecraftTitleHud;
import io.github.ottermc.screen.hud.minecraft.MinecraftTooltipHud;

public final class GameDisplay {

	public static final Component BOSS_BAR = new MinecraftBossBarHud();
	public static final Component CHAT = new MinecraftChatHud();
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
