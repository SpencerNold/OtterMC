package io.github.ottermc.screen.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import agent.Reflection;
import io.github.ottermc.modules.utility.Chat;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;

public class ChatScreen extends GuiChat {
	
	private final DrawableHelper drawable;
	private final Minecraft mc;

	private final List<ChatSection> openChatSections = new ArrayList<>();
	private int openChatIndex = 0;
	
	private String text = "";
	private int timer = 0;
	
	public ChatScreen(Minecraft mc) {
		this.drawable = new DrawableHelper();
		this.mc = mc;
		openChatSections.add(new ChatSection("Chat"));
		openChatSections.add(new ChatSection("Party"));
		openChatSections.add(new ChatSection("ILostAChromosome"));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int x = 2;
		int y = (int) (drawable.getHeight() - 14);
		
		drawable.fillRectangle(x, y, (int) (drawable.getWidth() - 4), 12, Integer.MIN_VALUE);
		
		drawString(text + (timer < 9 ? "_" : ""), x + 2, y + 2, -1);
		
		int width = (int) (drawable.getWidth() * 0.50) + 5;
		int height = 112;
		int rectY = y - height - 12;
		drawable.fillRectangle(0, rectY, width, height, Integer.MIN_VALUE);
		drawable.drawRectangle(0, rectY, width, height, 0xFFC6C6C6);
		
		GuiNewChat gui = mc.ingameGUI.getChatGUI();
		@SuppressWarnings("unchecked")
		List<ChatLine> list = (List<ChatLine>) Reflection.getMinecraftField("net/minecraft/client/gui/GuiNewChat", "field_146253_i", gui);
		for (int i = 0; i < list.size(); i++) {
			// TODO Add scrolling
			ChatLine line = list.get(i);
			int lineX = x;
			int lineY = y - (i * 9) - 22;
			if (lineY > rectY)
				drawString(line.getChatComponent().getFormattedText(), lineX, lineY, -1);
		}
		Color color = Chat.getColor();
		int offsX = 0;
		for (int i = 0; i < openChatSections.size(); i++) {
			ChatSection section = openChatSections.get(i);
			int nw = drawable.getStringWidth(section.name) + 5;
			int sy = rectY - 12;
			drawable.fillRectangle(3 + offsX, sy, nw, 12, (i == openChatIndex) ? color.getValue() : Integer.MIN_VALUE);
			drawable.drawString(section.name, offsX + 4, sy + 2, -1);
			offsX += nw + 5;
		}
	}
	
	@Override
	public void updateScreen() {
		if (timer < 0)
			timer = 20;
		timer--;
	}
	
	@Override
	public void keyTyped(char c, int key) throws IOException {
		if (key == Keyboard.KEY_BACK) {
			if (text.length() != 0)
				text = text.substring(0, text.length() - 1);
			return;
		}
		if (key == Keyboard.KEY_RETURN) {
			mc.thePlayer.sendChatMessage(text);
			text = "";
			mc.displayGuiScreen(null);
			return;
		}
		text += c;
		super.keyTyped(c, key);
	}
	
	private void drawString(String str, int x, int y, int color) {
		if (Chat.shouldUseClientFont())
			drawable.drawString(str, x, y, color);
		else
			mc.fontRendererObj.drawStringWithShadow(str, x, y, color);
	}
	
	private static class ChatSection {
		
		private final String name;
		
		public ChatSection(String name) {
			this.name = name;
		}
	}
}
