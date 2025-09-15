package io.github.ottermc.pvp.modules.hypixel;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.Writable;
import io.github.ottermc.modules.setting.StringSetting;
import io.github.ottermc.pvp.listeners.AddChatMessageListener;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.client.Minecraft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoGG extends HypixelModule implements RunTickListener, AddChatMessageListener {

	private static final Icon ICON = Icon.getIconIgnoreException("module/handshake_icon.png");

	private static final Pattern REGULAR_CHAT_PATTERN = Pattern.compile("(\\S* )?(\\S{1,16}): (.+)$");

	private final String[] gameEndStrings = new String[] {
			"1st Killer - ", "1st Place - ", "Winner: ",
			" - Damage Dealt - ", "Winning Team - ", "1st - ", "Winners: ", "Winner: ", "Winning Team: ",
			" won the game!", "Top Seeker: ", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors",
			"Winners - ", "WINNER!"
	};

	private final StringSetting gameEndMessage = new StringSetting("Message", "gg", 1);

	private int timer = -1;

	public AutoGG() {
		super("Auto GG");
	}

	@Override
	public void onEnable() {
		EventBus.add(this);
	}

	@Override
	public void onDisable() {
		EventBus.remove(this);
	}

	@Override
	public void onRunTick(RunTickEvent event) {
		if (!isConnectedToHypixel())
			return;
		if (timer > 0)
			timer--;
		if (timer == 0) {
			timer = -1;
			Minecraft mc = Minecraft.getMinecraft();
			mc.thePlayer.sendChatMessage("/ac " + gameEndMessage.getValue());
		}
	}

	@Override
	public void onAddChatMessage(AddChatMessageEvent event) {
		String message = event.getMessage();
		System.out.println("A" + message);
		Matcher matcher = REGULAR_CHAT_PATTERN.matcher(message);
		if (matcher.find())
			return;
		System.out.println("B" + message);
		for (String str : gameEndStrings) {
			if (message.contains(str))
				timer = 20;
		}
	}

	@Override
	public Writable<ByteBuf>[] getWritables() {
		return new Storable<?>[] { gameEndMessage };
	}

	@Override
	public Icon getIcon() {
		return ICON;
	}
}
