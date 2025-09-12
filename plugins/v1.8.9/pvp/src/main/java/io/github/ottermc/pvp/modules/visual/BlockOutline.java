package io.github.ottermc.pvp.modules.visual;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.BooleanSetting;
import io.github.ottermc.modules.setting.ColorSetting;
import io.github.ottermc.modules.setting.FloatSetting;
import io.github.ottermc.pvp.listeners.DrawSelectionBoxListener;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.render.Color;
import io.github.ottermc.render.VoxelRenderer;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class BlockOutline extends Module implements DrawSelectionBoxListener {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/block_icon.png");
	
	private final ColorSetting color = new ColorSetting("Color", Color.getDefault(), true);
	private final BooleanSetting fill = new BooleanSetting("Fill Block", false);
	private final FloatSetting thickness = new FloatSetting("Thickness", 1.5, 0.5, 3.0);

	public BlockOutline() {
		super("Block Outline", CategoryList.VISUAL);
	}
	
	@Override
	public void onEnable() {
		EventBus.add(this);
	}
	
	@Override
	public void onDisable() {
		EventBus.remove(this);
	}

	@Override
	public void onDrawSelectionBox(DrawSelectionBoxEvent event) {
		MovingObjectPosition target = (MovingObjectPosition) event.getTarget();
		if (event.getType() != 0 || target.typeOfHit != MovingObjectType.BLOCK)
			return;
		event.setCanceled(true);
		Color color = this.color.getValue();
		Minecraft mc = Minecraft.getMinecraft();
		Block block = ((World) ((Entity) mc.thePlayer).worldObj).getBlockState(target.getBlockPos()).getBlock();
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glLineWidth(thickness.getValue().floatValue());
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		AxisAlignedBB box = block.getSelectedBoundingBox(mc.theWorld, target.getBlockPos()).offset(-(mc.getRenderManager()).viewerPosX, -(mc.getRenderManager()).viewerPosY, -(mc.getRenderManager()).viewerPosZ).expand(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D);
		GlStateManager.color(color.getRedNormal(), color.getGreenNormal(), color.getBlueNormal(), color.getAlphaNormal());
		RenderGlobal.func_181561_a(box); // Renders an outlined box
		if (fill.getValue()) {
			GlStateManager.color(color.getRedNormal(), color.getGreenNormal(), color.getBlueNormal(), color.getAlphaNormal() * 0.2f);
			VoxelRenderer.drawFilledBoundingBox(box);
		}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
	}
	
	@Override
	public Storable<?>[] getWritables() {
		return new Storable<?>[] { color, fill, thickness };
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
}
