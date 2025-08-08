package io.github.ottermc.screen.hud.minecraft;

import agent.adapters.MinecraftClassNameAdapter;
import io.github.ottermc.screen.hud.Component;
import me.spencernold.transformer.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class MinecraftNauseaEffectHud extends Component {

    public MinecraftNauseaEffectHud() {
		super(true);
	}

	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		Entity player = mc.thePlayer;
		if (!((EntityLivingBase) player).isPotionActive(Potion.confusion)) {
			float f = ((EntityPlayerSP) player).prevTimeInPortal + (((EntityPlayerSP) player).timeInPortal - ((EntityPlayerSP) player).prevTimeInPortal) * partialTicks;
			if (f > 0.0F) {
				this.func_180474_b(gui, f, res);
			}
		}
	}
	
	private void func_180474_b(GuiIngame gui, float f, ScaledResolution res) {
        Reflection.call("net/minecraft/client/gui/GuiIngame", gui, "func_180474_be", "(FLnet/minecraft/client/gui/ScaledResolution;)V", f, res);
	}
}
