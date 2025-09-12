package io.github.ottermc.screen.hud;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Writable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public abstract class MovableComponent extends Component implements Writable<ByteBuf> {

	protected int offsetX = 0, offsetY = 0;
	protected float scale = 1.0f;

	public MovableComponent(int defaultX, int defaultY, int width, int height) {
		super(defaultX, defaultY, width, height);
	}

	public void setOffset(int x, int y) {
		this.offsetX = x;
		this.offsetY = y;
	}

	public int getXOffset() {
		return offsetX;
	}

	public int getYOffset() {
		return offsetY;
	}

	public abstract int getSerialId();
	
	public void setScale(float scale) {
		scale = MathHelper.clamp_float(scale, 0.0f, 3.0f);
	}

	public float getScale() {
		return scale;
	}

	public void enableTranslate() {
		GlStateManager.translate(getXOffset(), getYOffset(), 0);
		GlStateManager.scale(scale, scale, 0);
	}

	public void disableTranslate() {
		GlStateManager.translate(-getXOffset(), -getYOffset(), 0);
		float inv = 1.0f / scale;
		GlStateManager.scale(inv, inv, 0);
		// TODO Scale as well
		        /*
        if (this instanceof MovableComponent) {
            float scale = ((MovableComponent) this).scale;
            GlStateManager.scale(scale, scale, scale);
            float inv = 1.0f / scale;
            float x = getDefaultX() - (getDefaultX() * inv);
            float y = getDefaultY() - (getDefaultY() * inv);
            GlStateManager.translate(-x, -y, 0);
            if (!mc.gameSettings.showDebugInfo)
                draw(mc, gui, partialTicks);
            GlStateManager.translate(x, y, 0);
            GlStateManager.scale(inv, inv, inv);
        } else {
            if (!mc.gameSettings.showDebugInfo)
                draw(mc, gui, partialTicks);
        }
        */
	}

	public abstract void drawDummyObject(Minecraft mc, Gui gui, float partialTicks);
	
	public void write(ByteBuf buf) {
		buf.writeInt(offsetX);
		buf.writeInt(offsetY);
		buf.writeFloat(scale);
	}
	
	public void read(ByteBuf buf) {
		offsetX = buf.readInt();
		offsetY = buf.readInt();
		scale = buf.readFloat();
	}
}
