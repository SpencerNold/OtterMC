package io.github.ottermc.screen.hud;

import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.lwjgl.opengl.GL11;

import io.github.ottermc.events.listeners.RenderGameOverlayListener;
import io.github.ottermc.screen.render.BlurShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class HudManager implements RenderGameOverlayListener {

	private final LinkedList<Component> components = new LinkedList<>();
	
	public void register(Component component) {
		components.add(component);
	}
	
	public <T extends Component> T getComponent(Class<T> clazz) {
		return clazz.cast(filter(c -> clazz.isInstance(c)).findAny().orElse(null));
	}
	
	public Stream<Component> filter(Predicate<Component> predicate) {
		return components.stream().filter(predicate);
	}
	
	public void iterate(Consumer<Component> consumer) {
		components.forEach(consumer);
	}

	@Override
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {
		event.setCanceled(true);
		if (BlurShaderProgram.isActive())
			return;
		Minecraft mc = Minecraft.getMinecraft();
		mc.entityRenderer.setupOverlayRendering();
		boolean tex2d = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		components.forEach(c -> c.drawComponent(mc, event.getGuiIngame(), new ScaledResolution(mc), event.getPartialTicks()));
		if (tex2d)
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		else
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		GlStateManager.enableBlend();
	}
}
