package io.github.ottermc.screen;

import java.io.IOException;

import io.github.ottermc.Client;
import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;

public abstract class AbstractScreen extends GuiScreen {
	
	private final DrawableHelper drawable = new DrawableHelper();
	
	protected final int backgroundColor = 0x400D1A22;
	
	@Override
	public final void initGui() {
		super.initGui();
		onScreenOpen();
	}
	
	@Override
	public final void onGuiClosed() {
		super.onGuiClosed();
		onScreenClose();
	}
	
	@Override
	public final void onResize(Minecraft mc, int sx, int sy) {
		super.onResize(mc, sx, sy);
		onResize(sx, sy);
	}
	
	@Override
	public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
		getDrawable().setupOverlayRendering();
		renderScreen(mouseX, mouseY, partialTicks);
		String text = String.format("%s %s", Client.NAME, Client.VERSION);
		getDrawable().drawString(text, getDisplayWidth() - getDrawable().getStringWidth(text) - 4, getDisplayHeight() - getDrawable().getStringHeight() - 4, -1);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public final void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClicked(mouseX, mouseY, button);
		onClick(mouseX, mouseY, button);
	}
	
	@Override
	public final void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		onRelease(mouseX, mouseY, state);
	}
	
	@Override
	public final void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		onClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	@Override
	public final void keyTyped(char typedChar, int keyCode) throws IOException {
		boolean b = onKeyPressed(typedChar, keyCode);
		if (b)
			super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public final void updateScreen() {
		super.updateScreen();
		onUpdate();
	}
	
	@Override
	public final boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		onHandleMouseInput();
	}
	
	public abstract void onScreenOpen();
	public abstract void onScreenClose();
	public void renderScreen(int mouseX, int mouseY, float partialTicks) {}

	public void onResize(int sx, int sy) {}
	public void onClick(int mouseX, int mouseY, int button) {}
	public void onRelease(int mouseX, int mouseY, int button) {}
	public void onClickMove(int mouseX, int mouseY, int button, long dt) {}
	public boolean onKeyPressed(char c, int key) { return true; }
	public void onUpdate() {}
	public void onHandleMouseInput() {}
	
	public int getDisplayWidth() {
		return MathHelper.ceiling_double_int(getDrawable().getWidth());
	}
	
	public int getDisplayHeight() {
		return MathHelper.ceiling_double_int(getDrawable().getHeight());
	}
	
	public int getRawWidth() {
		return super.width;
	}
	
	public int getRawHeight() {
		return super.height;
	}

	public DrawableHelper getDrawable() {
		return drawable;
	}
	
	public int getBackgroundColor() {
		return backgroundColor;
	}
}
