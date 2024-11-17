package io.github.ottermc.screen.impl;

import io.github.ottermc.screen.AbstractScreen;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class MenuScreen extends AbstractScreen {
	
	@Override
	public void onScreenOpen() {
		getDrawable().setScale(2.0f);
		BlurShaderProgram.setActive(true, true);
	}

	@Override
	public void onScreenClose() {
		BlurShaderProgram.setActive(false, false);
	}
	
	@Override
	public void renderScreen(int mouseX, int mouseY, float partialTicks) {
		int length = 42;
		int centerX = getDrawable().middle(getDisplayWidth(), length);
		int centerY = getDrawable().middle(getDisplayHeight(), length);
		int x;
		boolean hovering;
		boolean shouldDrawHovering = false;
		// Box 1
		x = centerX - 48;
		hovering = getDrawable().intersects(x, centerY, length, length, mouseX, mouseY);
		getDrawable().fillRoundedRectangle(x, centerY, length, length, 8, hovering ? 0xA00D1A22 : backgroundColor);
		getDrawable().drawIcon(Icon.HAT, centerX - 43, centerY + 5, 0.5f, 0xB0C6C6C6); // 0xC6101010?
		shouldDrawHovering = shouldDrawHovering || hovering;
		// Box 2
		x = centerX;
		hovering = getDrawable().intersects(x, centerY, length, length, mouseX, mouseY);
		getDrawable().fillRoundedRectangle(x, centerY, length, length, 8, hovering ? 0xA00D1A22 :  backgroundColor);
		getDrawable().drawIcon(Icon.GEAR, centerX + 5, centerY + 5, 0.5f, 0xB0C6C6C6); // 0xC6101010?
		shouldDrawHovering = shouldDrawHovering || hovering;
		// Box 3
		x = centerX + 48;
		hovering = getDrawable().intersects(x, centerY, length, length, mouseX, mouseY);
		getDrawable().fillRoundedRectangle(x, centerY, length, length, 8, hovering ? 0xA00D1A22 : backgroundColor);
		getDrawable().drawIcon(Icon.MOVE, centerX + 53, centerY + 5, 0.5f, 0xB0C6C6C6); // 0xC6101010?
		shouldDrawHovering = shouldDrawHovering || hovering;
	}
	
	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		int length = 42;
		int centerX = getDrawable().middle(getDisplayWidth(), length);
		int centerY = getDrawable().middle(getDisplayHeight(), length);
		int x;
		// Box 1
		x = centerX - 48;
		if (getDrawable().intersects(x, centerY, length, length, mouseX, mouseY)) {
			// Open cosmetics menu
		}
		// Box 2
		x = centerX;
		if (getDrawable().intersects(x, centerY, length, length, mouseX, mouseY))
			Minecraft.getMinecraft().displayGuiScreen(new ModScreen());
		// Box3
		x = centerX + 48;
		if (getDrawable().intersects(x, centerY, length, length, mouseX, mouseY))
			Minecraft.getMinecraft().displayGuiScreen(new EditHudScreen());
	}
}
