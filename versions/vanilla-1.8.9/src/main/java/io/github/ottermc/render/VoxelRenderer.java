package io.github.ottermc.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;

public class VoxelRenderer {

	public static void drawFilledBoundingBox(AxisAlignedBB box) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.begin(7, DefaultVertexFormats.POSITION);
		renderer.pos(box.minX, box.minY, box.minZ).endVertex();
		renderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		renderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		renderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		renderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		tessellator.draw();
		renderer.begin(7, DefaultVertexFormats.POSITION);
		renderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		renderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		renderer.pos(box.minX, box.minY, box.minZ).endVertex();
		renderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		renderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		renderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		tessellator.draw();
		renderer.begin(7, DefaultVertexFormats.POSITION);
		renderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		renderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		renderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		renderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		renderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		renderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		tessellator.draw();
		renderer.begin(7, DefaultVertexFormats.POSITION);
		renderer.pos(box.minX, box.minY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		renderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		renderer.pos(box.minX, box.minY, box.minZ).endVertex();
		renderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		tessellator.draw();
		renderer.begin(7, DefaultVertexFormats.POSITION);
		renderer.pos(box.minX, box.minY, box.minZ).endVertex();
		renderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		renderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		renderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		renderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		tessellator.draw();
		renderer.begin(7, DefaultVertexFormats.POSITION);
		renderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		renderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		renderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		renderer.pos(box.minX, box.minY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		renderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		renderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		tessellator.draw();
	}
}
