package io.github.ottermc.screen.impl;

import io.github.ottermc.Client;
import io.github.ottermc.ClientLogger;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.AbstractScreen;
import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.HudManager;
import io.github.ottermc.screen.hud.MovableComponent;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class EditHudScreen extends AbstractScreen {

    private final HudManager manager;

    private MovableComponent selected = null;
    private MovableComponent lastClicked = null;
    private int clickOffsX = 0, clickOffsY = 0;

    public EditHudScreen() {
        this.manager = Client.getInstance().getHudManager();
    }

    @Override
    public void onScreenOpen() {
        BlurShaderProgram.setActive(true, true);
    }

    @Override
    public void onScreenClose() {
        BlurShaderProgram.setActive(false, false);
        try {
            Client.getInstance().save();
        } catch (IOException e) {
            ClientLogger.display(e);
        }
    }

    @Override
    public void renderScreen(int mouseX, int mouseY, float partialTicks) {
        handleSelectedMouseMovement(mouseX, mouseY);
        DrawableHelper drawable = getDrawable();
        for (Component c : manager.getComponents()) {
            if (!(c instanceof MovableComponent) || !c.isVisible())
                continue;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            MovableComponent component = (MovableComponent) c;
            component.enableTranslate();
            component.drawDummyObject(Minecraft.getMinecraft(), this, partialTicks);
            int x = component.getDefaultX();
            int y = component.getDefaultY();
            int width = component.getRawWidth();
            int height = component.getRawHeight();
            drawable.drawRectangle(x - 2, y - 2, width + 4, height + 4, selected == c ? Color.getDefault().getValue() : 0xFFF5F5F5);
            component.disableTranslate();
        }
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) {
        selected = null;
        DrawableHelper drawable = getDrawable();
        int x = 0, y = 0, width = 0, height = 0;
        for (Component c : manager.getComponents()) {
            if (c instanceof MovableComponent && c.isVisible()) {
                MovableComponent component = (MovableComponent) c;
                x = component.getDefaultX() + component.getXOffset();
                y = component.getDefaultY() + component.getYOffset();
                width = component.getRawWidth();
                height = component.getRawHeight();
                if (drawable.intersects(x, y, width, height, mouseX, mouseY)) {
                    selected = component;
                    break;
                }
            }
        }
        if (selected != null) {
            clickOffsX = mouseX - x;
            clickOffsY = mouseY - y;
        }
        lastClicked = selected;
    }

    private void handleSelectedMouseMovement(int mouseX, int mouseY) {
        if (selected != null) {
            int x = mouseX - clickOffsX - selected.getDefaultX();
            int y = mouseY - clickOffsY - selected.getDefaultY();
            selected.setOffset(x, y);
        }
    }

    @Override
    public void onHandleMouseInput() {
        int dx = Mouse.getEventDWheel();
        if (dx != 0 && lastClicked != null) {
            float scale = lastClicked.getScale() + ((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 0.05f : 0.01f) * (dx < 0 ? -1 : 1));
            lastClicked.setScale(scale);
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        selected = null;
    }

    /*

	private Component lastClicked = null;
	private int clickOffsX = 0, clickOffsY = 0;

	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		selected = getMoveable().filter(c -> {
			return getDrawable().intersectsRaw(c.getDefaultX(), c.getDefaultY(), c.getWidth(), c.getHeight(), mouseX, mouseY);
		}).findAny();
		if (selected.isPresent()) {
			Component c = selected.get();
			clickOffsX = (int) (mouseX - c.getDefaultX());
			clickOffsY = (int) (mouseY - c.getDefaultY());
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
		if (dx != 0 && lastClicked != null && lastClicked instanceof MovableComponent) {
			((MovableComponent) lastClicked).setScale(lastClicked.getScale() + ((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 0.05f : 0.01f) * (dx < 0 ? -1 : 1)));
		}
	}
	
	@Override
	public void renderScreen(int mouseX, int mouseY, float partialTicks) {
		int color = ClientTheme.getColor().getValue();
		
		Minecraft mc = Minecraft.getMinecraft();
		boolean tex2d = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if (selected.isPresent()) {
			float inv = 1.0f / scale;
			Component component = selected.get();
			((MovableComponent) component).setX(mouseX - clickOffsX);
			((MovableComponent) component).setY(mouseY - clickOffsY);
			((MovableComponent) component).clamp((int) (getDisplayWidth() * inv), (int) (getDisplayHeight() * inv));
		}
		Stream<Component> components = getMoveable();
		MutRef<Boolean> isEmpty = new MutRef<>(true);
		components.forEach(c -> {
			if (isEmpty.get())
				isEmpty.set(false);
			GlStateManager.scale(2.0f, 2.0f, 2.0f);
			c.drawComponent(mc, mc.ingameGUI, 0.0f);
			GlStateManager.scale(0.5f, 0.5f, 0.5f);
			getDrawable().drawRectangle(c.getDefaultX() * scale - 3, c.getDefaultY() * scale - 3, c.getWidth() * scale + 6, c.getHeight() * scale + 6, c == lastClicked ? color : 0xFFC6C6C6);
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
			int y = lastClicked.getDefaultY() - 13;
			String text = ((int) (lastClicked.getScale() * 100)) + "%";
			if (ttf) {
				int width = getDrawable().getStringWidth(text, 0.5f);
				getDrawable().fillRectangle(lastClicked.getDefaultX() - 2, y - 2, width + 4, 13, getBackgroundColor());
				getDrawable().drawString(text, lastClicked.getDefaultX() - 1, y, 0.5f, -1);
			} else {
				int width = mc.fontRendererObj.getStringWidth(text);
				getDrawable().fillRectangle(lastClicked.getDefaultX() - 2, y - 2, width + 4, 13, getBackgroundColor());
				mc.fontRendererObj.drawString(text, lastClicked.getDefaultX() + 1, y + 1, -1);
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
			return c instanceof MovableComponent && c.isVisible() && c.getRawWidth() != 0 && c.getRawHeight() != 0;
		});
	}
 */
}
