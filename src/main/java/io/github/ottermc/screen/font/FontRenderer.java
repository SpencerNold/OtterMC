package io.github.ottermc.screen.font;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;

public class FontRenderer {

	public static final FontRenderer OMC_TTF_RENDERER = new FontRenderer(Font.getFontIgnoreException("/font/omc_ttf_font.png"), new Charset("/font/omc_ttf_charset.json"));
	
	private final Font font;
	private final Charset charset;

	public FontRenderer(Font font, Charset charset) {
		this.font = font;
		this.charset = charset;
	}
	
	public int getStringWidth(String text) {
		float width = 0;
		for (char c : text.toCharArray()) {
			if (c == ' ') {
				width += 8.0f;
				continue;
			}
			width += charset.getData(c)[2] - 10.0f;
		}
		return (int) width;
	}
	
	public int getStringHeight() {
		return 36;
	}

	public void renderText(String text, float x, float y, int color) {
		if (text == null)
			return;
		GlStateManager.pushMatrix();
		GlStateManager.enableAlpha();
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
		GlStateManager.bindTexture(font.getTextureId());
		int width = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == ' ') {
				width += 8.0f;
				continue;
			}
			width += renderCharacter(c, x + width, y) - 10.0f;
		}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.popMatrix();
	}
	
	private int renderCharacter(char c, float x, float y) {
		int[] data = charset.getData(c);
		drawTexturedQuad(x, y, data[2], data[3], data[0], data[1], data[2], data[3]);
		return data[2];
	}
	
	private void drawTexturedQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
		float renderSRCX = srcX / 512;
		float renderSRCY = srcY / 512;
		float renderSRCWidth = srcWidth / 512;
		float renderSRCHeight = srcHeight / 512;
		GL11.glBegin(4);
		GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		GL11.glVertex2d((x + width), y);
		GL11.glTexCoord2f(renderSRCX, renderSRCY);
		GL11.glVertex2d(x, y);
		GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		GL11.glVertex2d(x, (y + height));
		GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		GL11.glVertex2d(x, (y + height));
		GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
		GL11.glVertex2d((x + width), (y + height));
		GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		GL11.glVertex2d((x + width), y);
		GL11.glEnd();
	}
	
	public boolean isCharacterSupported(char c) {
		return charset.isCharacterSupported(c);
	}
}
