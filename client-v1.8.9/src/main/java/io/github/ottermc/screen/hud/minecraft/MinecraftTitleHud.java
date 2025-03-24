package io.github.ottermc.screen.hud.minecraft;

import io.github.ottermc.screen.hud.Component;
import me.spencernold.transformer.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class MinecraftTitleHud extends Component {
	
	public MinecraftTitleHud() {
		super(true);
	}

	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		int field_175195_w = getField_175195_w(gui);
        if (field_175195_w > 0)
        {
            mc.mcProfiler.startSection("titleAndSubtitle");
            float f3 = (float) field_175195_w - partialTicks;
            int i2 = 255;

            int field_175193_B = getField_175193_B(gui);
            int field_175192_A = getField_175192_A(gui);
            int field_175199_z = getField_175199_z(gui);
            if (field_175195_w > field_175193_B + field_175192_A)
            {
                float f4 = (float)(field_175199_z + field_175192_A + field_175193_B) - f3;
                i2 = (int)(f4 * 255.0F / (float)field_175199_z);
            }

            if (field_175195_w <= field_175193_B)
            {
                i2 = (int)(f3 * 255.0F / (float)field_175193_B);
            }

            i2 = MathHelper.clamp_int(i2, 0, 255);

            if (i2 > 8)
            {
            	String field_175201_x = getField_175201_x(gui);
            	String field_175200_y = getField_175200_y(gui);
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(res.getScaledWidth() / 2), (float)(res.getScaledHeight() / 2), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0F, 4.0F, 4.0F);
                int j2 = i2 << 24 & -16777216;
                mc.fontRendererObj.drawString(field_175201_x, (float)(-mc.fontRendererObj.getStringWidth(field_175201_x) / 2), -10.0F, 16777215 | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                mc.fontRendererObj.drawString(field_175200_y, (float)(-mc.fontRendererObj.getStringWidth(field_175200_y) / 2), 5.0F, 16777215 | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            mc.mcProfiler.endSection();
        }		
	}

	private String getField_175200_y(GuiIngame gui) {
		return (String) Reflection.getValue(GuiIngame.class, gui, "field_175200_y");
	}

	private String getField_175201_x(GuiIngame gui) {
		return (String) Reflection.getValue(GuiIngame.class, gui, "field_175201_x");
	}

	private int getField_175199_z(GuiIngame gui) {
		return (int) Reflection.getValue(GuiIngame.class, gui, "field_175199_z");
	}

	private int getField_175192_A(GuiIngame gui) {
		return (int) Reflection.getValue(GuiIngame.class, gui, "field_175192_A");
	}

	private int getField_175193_B(GuiIngame gui) {
		return (int) Reflection.getValue(GuiIngame.class, gui, "field_175193_B");
	}

	private int getField_175195_w(GuiIngame gui) {
		return (int) Reflection.getValue(GuiIngame.class, gui, "field_175195_w");
	}
}
