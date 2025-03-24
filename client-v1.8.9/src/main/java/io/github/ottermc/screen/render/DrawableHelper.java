package io.github.ottermc.screen.render;

import org.lwjgl.opengl.GL11;

import io.github.ottermc.screen.font.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;

public class DrawableHelper {

	private final FontRenderer fontRenderer = FontRenderer.OMC_TTF_RENDERER;

	private float scale = 1.0f;
	
	public void setupOverlayRendering() {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		GlStateManager.clear(256);
		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, res.getScaledWidth_double() * scale, res.getScaledHeight_double() * scale, 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
	}
	
	public boolean isFontCharSupported(char c) {
		return fontRenderer.isCharacterSupported(c);
	}

	public void drawString(String text, int x, int y, float scale, int color) {
		float ratio = this.scale * 0.25f * scale;
		float inv = 1.0f / ratio;
		x = (int) ((float) x * inv);
		y = (int) ((float) y * inv);
		GlStateManager.scale(ratio, ratio, ratio);
		fontRenderer.renderText(text, x, y, color);
		GlStateManager.scale(inv, inv, inv);
	}

	public void drawString(String text, int x, int y, int color) {
		drawString(text, x, y, 1.0f, color);
	}

	public int getStringWidth(String text, float scale) {
		return (int) (fontRenderer.getStringWidth(text) * (this.scale * 0.25f * scale));
	}

	public int getStringHeight(float scale) {
		return (int) (fontRenderer.getStringHeight() * (this.scale * 0.25f * scale));
	}

	public int getStringWidth(String text) {
		return getStringWidth(text, 1.0f);
	}

	public int getStringHeight() {
		return getStringHeight(1.0f);
	}

	public void fillRectangle(int x, int y, int width, int height, int color) {
		Gui.drawRect(x, y, x + width, y + height, color);
	}

	public void drawRectangle(int x, int y, int width, int height, int color) {
		drawHorizontalLine(x, x + width, y, color);
		drawHorizontalLine(x, x + width, y + height - 1, color);
		drawVerticalLine(x, y + 1, y + height - 1, color);
		drawVerticalLine(x + width - 1, y + 1, y + height - 1, color);
	}
	
	public void outlineRectangle(int x, int y, int width, int height, int innerColor, int outerColor) {
		fillRectangle(x, y, width, height, innerColor);
		drawRectangle(x, y, width, height, outerColor);
	}

	public void fillRoundedRectangle(int posX, int posY, int width, int height, int radius, int color) {
		fillRectangle(posX + radius, posY, width - (radius * 2), radius, color);
		fillRectangle(posX, posY + radius, width, height - (radius * 2), color);
		fillRectangle(posX + radius, posY + height - radius, width - (radius * 2), radius, color);
		for (int x = 0; x < radius; x++) {
			for (int y = 0; y < radius; y++) {
				// Corner 1
				int dx = x - radius + 1;
				int dy = y - radius + 1;
				int dist = MathHelper.floor_float(MathHelper.sqrt_float(dx * dx + dy * dy));
				if (dist < radius)
					fillPixel(posX + x, posY + y, color);
				// Corner 2
				dx = x;
				dist = MathHelper.floor_float(MathHelper.sqrt_float(dx * dx + dy * dy));
				if (dist < radius)
					fillPixel(posX + width + x - radius, posY + y, color);
				// Corner 3
                dy = y;
				dist = MathHelper.floor_float(MathHelper.sqrt_float(dx * dx + dy * dy));
				if (dist < radius)
					fillPixel(posX + width + x - radius, posY + height + y - radius, color);
				// Corner 4
				dx = x - radius + 1;
				dist = MathHelper.floor_float(MathHelper.sqrt_float(dx * dx + dy * dy));
				if (dist < radius)
					fillPixel(posX + x, posY + height + y - radius, color);
			}
		}
	}

	public void fillTopRoundedRectangle(int posX, int posY, int width, int height, int radius, int color) {
		fillRectangle(posX + radius, posY, width - (radius * 2), radius, color);
		fillRectangle(posX, posY + radius, width, height - radius, color);
		for (int x = 0; x < radius; x++) {
			for (int y = 0; y < radius; y++) {
				// Corner 1
				int dx = x - radius + 1;
				int dy = y - radius + 1;
				int dist = MathHelper.floor_float(MathHelper.sqrt_float(dx * dx + dy * dy));
				if (dist < radius)
					fillPixel(posX + x, posY + y, color);
				// Corner 2
				dx = x;
				dist = MathHelper.floor_float(MathHelper.sqrt_float(dx * dx + dy * dy));
				if (dist < radius)
					fillPixel(posX + width + x - radius, posY + y, color);
			}
		}
	}

	public void drawRoundedRectangle(int posX, int posY, int width, int height, int radius, int color) {
		drawHorizontalLine(posX + radius, posX + width - radius, posY, color);
		drawHorizontalLine(posX + radius, posX + width - radius, posY + height - 1, color);
		drawVerticalLine(posX, posY + radius, posY + height - radius, color);
		drawVerticalLine(posX + width - 1, posY + radius, posY + height - radius, color);
		for (int x = 0; x < radius; x++) {
			for (int y = 0; y < radius; y++) {
				// Corner 1
				int dx = x - radius + 1;
				int dy = y - radius + 1;
				int dist = MathHelper.floor_float(MathHelper.sqrt_float(dx * dx + dy * dy));
				if (dist == radius - 1)
					fillPixel(posX + x, posY + y, color);
				// Corner 2
				dx = x;
				dist = MathHelper.floor_float(MathHelper.sqrt_float(dx * dx + dy * dy));
				if (dist == radius - 1)
					fillPixel(posX + width + x - radius, posY + y, color);
				// Corner 3
                dy = y;
				dist = MathHelper.floor_float(MathHelper.sqrt_float(dx * dx + dy * dy));
				if (dist == radius - 1)
					fillPixel(posX + width + x - radius, posY + height + y - radius, color);
				// Corner 4
				dx = x - radius + 1;
				dist = MathHelper.floor_float(MathHelper.sqrt_float(dx * dx + dy * dy));
				if (dist == radius - 1)
					fillPixel(posX + x, posY + height + y - radius, color);
			}
		}
	}

	public void drawIcon(Icon icon, int x, int y, float scale, int color) {
		scale *= this.scale * 0.5f;
		GlStateManager.pushMatrix();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.enableTexture2D();
		if ((color & 0xFC000000) == 0)
			color |= 0xFF000000;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		GlStateManager.color(red, green, blue, alpha);
		GlStateManager.bindTexture(icon.getTextureId());
		GlStateManager.scale(scale, scale, scale);
		float sinv = 1.0f / scale;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		Gui.drawModalRectWithCustomSizedTexture((int) (x * sinv), (int) (y * sinv), 0, 0, 64, 64, 64, 64);
		GlStateManager.scale(sinv, sinv, sinv);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.disableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.popMatrix();
	}

	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(x, y + height, 100.0).tex((float) (textureX) * f, (float) (textureY + height) * f1).endVertex();
		renderer.pos(x + width, y + height, 100.0).tex((float) (textureX + width) * f, (float) (textureY + height) * f1).endVertex();
		renderer.pos(x + width, y, 100.0).tex((float) (textureX + width) * f, (float) (textureY) * f1).endVertex();
		renderer.pos(x, y, 100.0).tex((float) (textureX) * f, (float) (textureY) * f1).endVertex();
		tessellator.draw();
	}

	public void drawIcon(Icon icon, int x, int y, int color) {
		drawIcon(icon, x, y, 1.0f, color);
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}

	public double getWidth() {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		return res.getScaledWidth_double() * scale;
	}

	public double getHeight() {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		return res.getScaledHeight_double() * scale;
	}

	public boolean intersects(int x, int y, int width, int height, int mouseX, int mouseY) {
		mouseX = (int) ((float) mouseX * scale);
		mouseY = (int) ((float) mouseY * scale);
		return intersectsRaw(x, y, width, height, mouseX, mouseY);
	}

	public boolean intersectsRaw(int x, int y, int width, int height, int mouseX, int mouseY) {
		return mouseX > x && mouseX < (x + width) && mouseY > y && mouseY < (y + height);
	}

	public int middle(int x1, int x2) {
		return (x1 / 2) - (x2 / 2);
	}

	private void fillPixel(int x, int y, int color) {
		Gui.drawRect(x, y, x + 1, y + 1, color);
	}

	public void drawHorizontalLine(int startX, int endX, int y, int color) {
		Gui.drawRect(startX, y, endX, y + 1, color);
	}

	public void drawVerticalLine(int x, int startY, int endY, int color) {
		Gui.drawRect(x, startY, x + 1, endY, color);
	}
}
