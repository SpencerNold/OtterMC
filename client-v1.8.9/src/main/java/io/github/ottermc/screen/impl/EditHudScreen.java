package io.github.ottermc.screen.impl;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import io.github.ottermc.ClientLogger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import io.github.ottermc.Client;
import io.github.ottermc.modules.visual.ColorTheme;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.AbstractScreen;
import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.HudManager;
import io.github.ottermc.screen.hud.Movable;
import io.github.ottermc.screen.render.BlurShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import structures.MutRef;

public class EditHudScreen extends AbstractScreen {

	private final int scale = 2;

	private final HudManager hudManager;

	private Optional<Component> selected = Optional.empty();
	private Component lastClicked = null;
	private int clickOffsX = 0, clickOffsY = 0;

	public EditHudScreen() {
		hudManager = Client.getHudManager();
	}

	@Override
	public void onScreenOpen() {
		getDrawable().setScale(scale);
		BlurShaderProgram.setActive(true, true);
	}

	@Override
	public void onScreenClose() {
		BlurShaderProgram.setActive(false, false);
		try {
			Client.getClientStorage().write();
		} catch (IOException e) {
			ClientLogger.display(e);
		}
	}

	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		selected = getMoveable().filter(c -> {
			return getDrawable().intersectsRaw(c.getX(), c.getY(), c.getWidth(), c.getHeight(), mouseX, mouseY);
		}).findAny();
		if (selected.isPresent()) {
			Component c = selected.get();
			clickOffsX = (int) (mouseX - c.getX());
			clickOffsY = (int) (mouseY - c.getY());
		}
		lastClicked = selected.orElse(null);
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int button) {
		selected = Optional.empty();
	}
	
	@Override
	public void onHandleMouseInput() {
		int dx = Mouse.getEventDWheel();
		if (dx != 0 && lastClicked != null && lastClicked instanceof Movable) {
			((Movable) lastClicked).setScale(lastClicked.getScale() + ((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 0.05f : 0.01f) * (dx < 0 ? -1 : 1)));
		}
	}
	
	@Override
	public void renderScreen(int mouseX, int mouseY, float partialTicks) {
		int color = (ColorTheme.isModActive() ? ColorTheme.getColorTheme() : Color.DEFAULT).getValue();
		
		Minecraft mc = Minecraft.getMinecraft();
		boolean tex2d = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if (selected.isPresent()) {
			float inv = 1.0f / scale;
			Component component = selected.get();
			((Movable) component).setX(mouseX - clickOffsX);
			((Movable) component).setY(mouseY - clickOffsY);
			((Movable) component).clamp((int) (getDisplayWidth() * inv), (int) (getDisplayHeight() * inv));
		}
		Stream<Component> components = getMoveable();
		MutRef<Boolean> isEmpty = new MutRef<>(true);
		components.forEach(c -> {
			if (isEmpty.get())
				isEmpty.set(false);
			GlStateManager.scale(2.0f, 2.0f, 2.0f);
			c.drawComponent(mc, mc.ingameGUI, new ScaledResolution(mc), 0.0f);
			GlStateManager.scale(0.5f, 0.5f, 0.5f);
			getDrawable().drawRectangle(c.getX() * scale - 3, c.getY() * scale - 3, c.getWidth() * scale + 6, c.getHeight() * scale + 6, c == lastClicked ? color : 0xFFC6C6C6);
		});
		if (isEmpty.get()) {
			String text = "Enable mods to edit HUD positions";
			int x = getDrawable().middle((int) getDrawable().getWidth(), getDrawable().getStringWidth(text));
			int y = getDrawable().middle((int) getDrawable().getHeight(), getDrawable().getStringHeight());
			getDrawable().drawString(text, x, y, -1);
		}
		if (lastClicked != null) {
			GlStateManager.scale(2.0f, 2.0f, 2.0f);
			boolean ttf = false; // TODO Make this a toggleable setting?
			int y = lastClicked.getY() - 13;
			String text = ((int) (lastClicked.getScale() * 100)) + "%";
			if (ttf) {
				int width = getDrawable().getStringWidth(text, 0.5f);
				getDrawable().fillRectangle(lastClicked.getX() - 2, y - 2, width + 4, 13, getBackgroundColor());
				getDrawable().drawString(text, lastClicked.getX() - 1, y, 0.5f, -1);
			} else {
				int width = mc.fontRendererObj.getStringWidth(text);
				getDrawable().fillRectangle(lastClicked.getX() - 2, y - 2, width + 4, 13, getBackgroundColor());
				mc.fontRendererObj.drawString(text, lastClicked.getX() + 1, y + 1, -1);
			}
			GlStateManager.scale(0.5f, 0.5f, 0.5f);
		}
		if (tex2d)
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		else
			GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	private Stream<Component> getMoveable() {
		return hudManager.filter(c -> {
			return c instanceof Movable && c.isVisible() && c.getRawWidth() != 0 && c.getRawHeight() != 0;
		});
	}
}
