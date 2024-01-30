package io.github.ottermc.screen.render;

import java.io.IOException;

import io.github.ottermc.events.listeners.RenderWorldListener;
import io.github.ottermc.events.listeners.UpdateDisplayListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

public class BlurShaderProgram implements RenderWorldListener, UpdateDisplayListener {

	private static final ResourceLocation RESOURCE = new ResourceLocation("shaders/post/blur.json");

	private static BlurShaderProgram instance;
	
	private ShaderGroup shader;
	private boolean active, hideHud;
	
	public BlurShaderProgram() {
		instance = this;
	}

	@Override
	public void onRenderWorld(RenderWorldEvent event) {
		if (shader == null)
			load(Minecraft.getMinecraft());
		if (!active)
			return;
		GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		shader.loadShaderGroup(event.getPartialTicks());
		GlStateManager.popMatrix();
		GlStateManager.enableDepth(); // disabled in preLoadShader function
	}
	
	@Override
	public void onUpdateDisplay(UpdateDisplayEvent event) {
		if (shader != null) {
			Minecraft mc = Minecraft.getMinecraft();
			shader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
		}
	}
	
	private void load(Minecraft mc) {
		detach();
		try {
			shader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), RESOURCE);
			shader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void detach() {
		if (shader != null)
			shader.deleteShaderGroup();
		shader = null;
	}
	
	public static void setActive(boolean active, boolean hideHud) {
		instance.active = active;
		instance.hideHud = hideHud;
	}
	
	public static void setActive(boolean active) {
		setActive(active, false);
	}
	
	public static boolean isActive() {
		return instance.active;
	}
	
	public static boolean shouldHideHud() {
		return instance.hideHud;
	}
}
