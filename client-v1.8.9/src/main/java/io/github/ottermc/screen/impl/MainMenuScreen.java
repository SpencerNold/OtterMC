package io.github.ottermc.screen.impl;

import agent.Reflection;
import io.github.ottermc.Client;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.DrawMainMenuScreenListener;
import io.github.ottermc.screen.ClientTheme;
import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class MainMenuScreen extends GuiMainMenu {

	private final DrawableHelper drawable = new DrawableHelper();
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.disableAlpha();
		reflect_renderSkybox(mouseX, mouseY, partialTicks);
		GlStateManager.enableAlpha();

		int displayWidth = MathHelper.ceiling_double_int(drawable.getWidth());
		int displayHeight = MathHelper.ceiling_double_int(drawable.getHeight());

		drawable.fillRectangle(0, 0, displayWidth, displayHeight, 0x40000000);

		drawable.drawString(Client.NAME + " " + Client.VERSION, 5, displayHeight - 14, -1);

		String text0 = "Modified by ILostAChromosome";
		drawable.drawString(text0, displayWidth - drawable.getStringWidth(text0) - 5, displayHeight - 14 - 11,
				-1);

		String text1 = "Not affiliated with Microsoft or Mojang; Do Not Distribute!";
		drawable.drawString(text1, displayWidth - drawable.getStringWidth(text1) - 5, displayHeight - 14, -1);

		int clientColor = ClientTheme.getColor().getValue();

		int middleX = drawable.middle(displayWidth, 150);
		int middleY = drawable.middle(displayHeight, 20);

		boolean hovering;
		String text;
		int x, y;

		x = middleX;
		y = middleY - 30;
		text = "Singleplayer";
		hovering = drawable.intersects(x, y, 150, 20, mouseX, mouseY);
		drawable.outlineRectangle(x, y, 150, 20, 0x50000000, hovering ? clientColor : 0xFFC6C6C6);
		drawable.drawString(text, x + drawable.middle(150, drawable.getStringWidth(text)), y + 5,
				hovering ? clientColor : -1);

		y = middleY;
		text = "Multiplayer";
		hovering = drawable.intersects(x, y, 150, 20, mouseX, mouseY);
		drawable.outlineRectangle(x, y, 150, 20, 0x50000000, hovering ? clientColor : 0xFFC6C6C6);
		drawable.drawString(text, x + drawable.middle(150, drawable.getStringWidth(text)), y + 5,
				hovering ? clientColor : -1);

		y = middleY + 30;
		text = "Options";
		hovering = drawable.intersects(x, y, 70, 20, mouseX, mouseY);
		drawable.outlineRectangle(x, y, 70, 20, 0x50000000, hovering ? clientColor : 0xFFC6C6C6);
		drawable.drawString(text, middleX + drawable.middle(70, drawable.getStringWidth(text)), y + 5,
				hovering ? clientColor : -1);

		x = middleX + 80;
		text = "Quit";
		hovering = drawable.intersects(x, y, 70, 20, mouseX, mouseY);
		drawable.outlineRectangle(x, y, 70, 20, 0x50000000, hovering ? clientColor : 0xFFC6C6C6);
		drawable.drawString(text, x + drawable.middle(70, drawable.getStringWidth(text)), y + 5,
				hovering ? clientColor : -1);

		x = middleX;
		y = middleY + 60;
		text = "Settings & Cosmetics";
		hovering = drawable.intersects(x, y, 150, 20, mouseX, mouseY);
		drawable.outlineRectangle(x, y, 150, 20, 0x50000000, hovering ? clientColor : 0xFFC6C6C6);
		drawable.drawString(text, x + drawable.middle(150, drawable.getStringWidth(text)), y + 5, hovering ? clientColor : -1);

		DrawMainMenuScreenListener.DrawMainMenuScreenEvent event = new DrawMainMenuScreenListener.DrawMainMenuScreenEvent();
		EventBus.fire(event);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		Minecraft mc = Minecraft.getMinecraft();

		int displayWidth = MathHelper.ceiling_double_int(drawable.getWidth());
		int displayHeight = MathHelper.ceiling_double_int(drawable.getHeight());
		
		int middleX = drawable.middle(displayWidth, 150);
		int middleY = drawable.middle(displayHeight, 20);

		boolean hovering;
		int x, y;

		x = middleX;
		y = middleY - 30;
		hovering = drawable.intersects(x, y, 150, 20, mouseX, mouseY);
		if (hovering) {
			playPressSound();
			mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		y = middleY;
		hovering = drawable.intersects(x, y, 150, 20, mouseX, mouseY);
		if (hovering) {
			playPressSound();
			mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		y = middleY + 30;
		hovering = drawable.intersects(x, y, 70, 20, mouseX, mouseY);
		if (hovering) {
			playPressSound();
			mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
		}

		x = middleX + 80;
		hovering = drawable.intersects(x, y, 70, 20, mouseX, mouseY);
		if (hovering) {
			playPressSound();
			mc.shutdown();
		}

		x = middleX;
		y = middleY + 60;
		hovering = drawable.intersects(x, y, 150, 20, mouseX, mouseY);
		if (hovering) {
			playPressSound();
			// TODO Settings and cosmetic screen
		}
	}

	private void playPressSound() {
		Minecraft.getMinecraft().getSoundHandler()
				.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
	}
	
	private void reflect_renderSkybox(int mouseX, int mouseY, float partialTicks) {
		Reflection.invokeMinecraft("net/minecraft/client/gui/GuiMainMenu", "renderSkybox(IIF)V", this, mouseX, mouseY, partialTicks);
	}
}
