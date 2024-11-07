package io.github.ottermc.modules.hypixel.macro;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

public enum Macro {

	// Lobby
	LOBBY("Lobby", mc -> {
		mc.thePlayer.sendChatMessage("/lobby");
	}),
	// Bedwars
	SOLO_BEDWARS("Solo Bedwars", mc -> {
		mc.thePlayer.sendChatMessage("/play bedwars_eight_one");
	}),
	DOUBLES_BEDWARS("Doubles Bedwars", mc -> {
		mc.thePlayer.sendChatMessage("/play bedwars_eight_two");
	}),
	TRIPPLES_BEDWARS("3v3v3v3 Bedwars", mc -> {
		mc.thePlayer.sendChatMessage("/play bedwars_four_three");
	}),
	QUADRUPLES_BEDWARS("4v4v4v4 Bedwars", mc -> {
		mc.thePlayer.sendChatMessage("/play bedwars_four_four");
	}),
	// Skywars
	INSANE_SKYWARS("Insane Skywars", mc -> {
		mc.thePlayer.sendChatMessage("/play solo_insane");
	}),
	NORMAL_SKYWARS("Normal Skywars", mc -> {
		mc.thePlayer.sendChatMessage("/play solo_normal");
	}),
	RANKED_SKYWARS("Ranked Skywars", mc -> {
		mc.thePlayer.sendChatMessage("/play ranked_normal");
	}),
	// Duels
	CLASSIC_DUELS("Classic Duels", mc -> {
		mc.thePlayer.sendChatMessage("/play duels_classic_duel");
	}),
	BRIDGE_DUELS("Bridge Duels", mc -> {
		mc.thePlayer.sendChatMessage("/play duels_bridge_duel");
	}),
	UHC_DUELS("UHC Duels", mc -> {
		mc.thePlayer.sendChatMessage("/play duels_uhc_duel");
	}),
	SKYWARS_DUELS("Skywars Duels", mc -> {
		mc.thePlayer.sendChatMessage("/play duels_sw_duel");
	}),
	SUMO_DUELS("Sumo Duels", mc -> {
		mc.thePlayer.sendChatMessage("/play duels_sumo_duel");
	}),
	// Skyblock
	SKYBLOCK("Skyblock", mc -> {
		mc.thePlayer.sendChatMessage("/play sb");
	}),
	// Murder Mystery
	MURDER_MYSTERY("Murder Mystery", mc -> {
		mc.thePlayer.sendChatMessage("/play murder_classic");
	});
	
	private final String displayName;
	private final Consumer<Minecraft> consumer;
	
	private Macro(String displayName, Consumer<Minecraft> consumer) {
		this.displayName = displayName;
		this.consumer = consumer;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void execute(Minecraft mc) {
		consumer.accept(mc);
	}
}
