package io.github.ottermc.modules.visual;

import org.lwjgl.opengl.GL11;

import agent.Reflection;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RenderItemInFirstPersonListener;
import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

// Sk1er and 1.7.10 MCP Mappings *insert pray emoji here*
// thank god for GPL
public class OldAnimation extends Module implements RenderItemInFirstPersonListener {

	private static final Icon ICON = Icon.getIconIgnoreException("module/sword_icon.png");

	private static final String className = "net/minecraft/client/renderer/ItemRenderer";

	public OldAnimation() {
		super("1.7 Animations", Category.VISUAL);
	}

	@Override
	public void onEnable() {
		EventBus.add(this);
	}

	@Override
	public void onDisable() {
		EventBus.remove(this);
	}

	private float getPrevEquippedProgress(ItemRenderer renderer) {
		return (float) Reflection.getMinecraftField(className, "prevEquippedProgress", renderer);
	}

	private float getEquippedProgress(ItemRenderer renderer) {
		return (float) Reflection.getMinecraftField(className, "equippedProgress", renderer);
	}

	private ItemStack getItemToRender(ItemRenderer renderer) {
		return (ItemStack) Reflection.getMinecraftField(className, "itemToRender", renderer);
	}

	private boolean isAlwaysEdible(ItemFood food) {
		return (boolean) Reflection.getMinecraftField("net/minecraft/item/ItemFood", "alwaysEdible", food);
	}

	@Override
	public void onRenderItemInFirstPerson(RenderItemInFirstPersonEvent event) {
		ItemRenderer renderer = (ItemRenderer) event.getRenderer();
		ItemStack itemToRender = getItemToRender(renderer);
		if (itemToRender != null) {
			float partialTicks = event.getPartialTicks();
			float prevEquippedProgress = getPrevEquippedProgress(renderer);
			float equippedProgress = getEquippedProgress(renderer);
			float progress = prevEquippedProgress + (equippedProgress - prevEquippedProgress) * partialTicks;
			event.setCanceled(renderItemInFirstPerson(renderer, itemToRender, progress, partialTicks));
		}
	}

	private boolean renderItemInFirstPerson(ItemRenderer renderer, ItemStack itemToRender, float progress, float partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		Item item = itemToRender.getItem();
		if (item == Items.filled_map || mc.getRenderItem().shouldRenderItemIn3D(itemToRender))
			return false;
		EnumAction action = itemToRender.getItemUseAction();
		Entity player = mc.thePlayer;
		float interpolatedPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
		GlStateManager.pushMatrix();
		GlStateManager.rotate(interpolatedPitch, 1, 0, 0);
		GlStateManager.rotate(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
		float interpolatedArmPitch = mc.thePlayer.prevRenderArmPitch + (mc.thePlayer.renderArmPitch - mc.thePlayer.prevRenderArmPitch) * partialTicks;
		float interpolatedArmYaw = mc.thePlayer.prevRenderArmYaw + (mc.thePlayer.renderArmYaw - mc.thePlayer.prevRenderArmYaw) * partialTicks;
		GlStateManager.rotate((player.rotationPitch - interpolatedArmPitch) * 0.1F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate((player.rotationYaw - interpolatedArmYaw) * 0.1F, 0.0F, 1.0F, 0.0F);
		GlStateManager.enableRescaleNormal();
		if (item instanceof ItemCloth) {
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		}
		int intensity = ((World) mc.theWorld).getCombinedLight(new BlockPos(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ), 0);
		float brightnessX = (float) (intensity & 65535);
		float brightnessY = (float) (intensity >> 16);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightnessX, brightnessY);
		int color = item.getColorFromItemStack(itemToRender, 0);
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		GlStateManager.color(red, green, blue, 1);
		GlStateManager.pushMatrix();
		int iteminUseCount = ((EntityPlayer) player).getItemInUseCount();
		float swingProgress = ((EntityLivingBase) Minecraft.getMinecraft().thePlayer).getSwingProgress(partialTicks);
		boolean ignoreBlockHitInstance = false;
		if (iteminUseCount <= 0 && mc.gameSettings.keyBindUseItem.isKeyDown()) {
			boolean block = action == EnumAction.BLOCK;
			boolean consume = false;
			if (item instanceof ItemFood) {
				boolean alwaysEdible = isAlwaysEdible((ItemFood) item);
				if (((EntityPlayer) player).canEat(alwaysEdible)) {
					consume = action == EnumAction.EAT || action == EnumAction.DRINK;
				}
			}
			if (block || consume)
				ignoreBlockHitInstance = true;
		}

		if ((iteminUseCount > 0 || ignoreBlockHitInstance) && action != EnumAction.NONE && ((EntityPlayer) player).isUsingItem()) {
			switch (action) {
			case EAT:
			case DRINK:
				float useAmount = (float) iteminUseCount - partialTicks + 1.0F;
				float useAmountNorm = 1.0F - useAmount / (float) itemToRender.getMaxItemUseDuration();
				float useAmountPow = 1.0F - useAmountNorm;
				useAmountPow = useAmountPow * useAmountPow * useAmountPow;
				useAmountPow = useAmountPow * useAmountPow * useAmountPow;
				useAmountPow = useAmountPow * useAmountPow * useAmountPow;
				float useAmountFinal = 1.0F - useAmountPow;
				GlStateManager.translate(0.0F, MathHelper.abs(MathHelper.cos(useAmount / 4.0F * (float) Math.PI) * 0.1F) * (float) ((double) useAmountNorm > 0.2D ? 1 : 0), 0.0F);
				GlStateManager.translate(useAmountFinal * 0.6F, -useAmountFinal * 0.5F, 0.0F);
				GlStateManager.rotate(useAmountFinal * 90.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(useAmountFinal * 10.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(useAmountFinal * 30.0F, 0.0F, 0.0F, 1.0F);
				rotateItemInProgress(progress, swingProgress);
				break;
			case BLOCK:
				rotateItemInProgress(progress, swingProgress);
				GlStateManager.translate(-0.5F, 0.2F, 0.0F);
				GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
				break;
			case BOW:
				rotateItemInProgress(progress, swingProgress);
				GlStateManager.rotate(-18.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(-12.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(-0.9F, 0.2F, 0.0F);
				float totalPullback = (float) itemToRender.getMaxItemUseDuration() - ((float) iteminUseCount - partialTicks + 1.0F);
				float pullbackNorm = totalPullback / 20.0F;
				pullbackNorm = (pullbackNorm * pullbackNorm + pullbackNorm * 2.0F) / 3.0F;
				if (pullbackNorm > 1.0F)
					pullbackNorm = 1.0F;
				if (pullbackNorm > 0.1F)
					GlStateManager.translate(0.0F, MathHelper.sin((totalPullback - 0.1F) * 1.3F) * 0.01F * (pullbackNorm - 0.1F), 0.0F);
				GlStateManager.translate(0.0F, 0.0F, pullbackNorm * 0.1F);
				GlStateManager.rotate(-335.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(-50.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.0F, 0.5F, 0.0F);
				float zScale = 1.0F + pullbackNorm * 0.2F;
				GlStateManager.scale(1.0F, 1.0F, zScale);
				GlStateManager.translate(0.0F, -0.5F, 0.0F);
				GlStateManager.rotate(50.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(335.0F, 0.0F, 0.0F, 1.0F);
			default:
				break;
			}
		} else {
			float swingProgress2 = MathHelper.sin(swingProgress * (float) Math.PI);
			float swingProgress3 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
			GlStateManager.translate(-swingProgress3 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI * 2.0F) * 0.2F, -swingProgress2 * 0.2F);
			rotateItemInProgress(progress, swingProgress);
		}
		if (item.shouldRotateAroundWhenRendering())
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		if (firstPerson3dRenderTransformation(mc, itemToRender))
			renderer.renderItem(mc.thePlayer, itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
		else
			renderer.renderItem(mc.thePlayer, itemToRender, ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
		if (item instanceof ItemCloth)
			GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		return true;
	}

	private void rotateItemInProgress(float progress, float swingProgress) {
		GlStateManager.translate(0.56f, -0.52f - (1.0F - progress) * 0.6F, -0.72f);
		GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
		float swingProgress2 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
		float swingProgress3 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
		GlStateManager.rotate(-swingProgress2 * 20.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-swingProgress3 * 20.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(-swingProgress3 * 80.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(0.4F, 0.4F, 0.4F);
	}

	private boolean firstPerson3dRenderTransformation(Minecraft mc, ItemStack stack) {
		GlStateManager.translate(0.58800083f, 0.36999986f, -0.77000016f);
		GlStateManager.translate(0, -0.3f, 0.0F);
		GlStateManager.scale(1.5f, 1.5f, 1.5f);
		GlStateManager.rotate(50.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(335.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.translate(-0.9375F, -0.0625F, 0.0F);
		GlStateManager.scale(-2, 2, -2);
		if (mc.getRenderItem().shouldRenderItemIn3D(stack)) {
			GlStateManager.scale(0.58823526f, 0.58823526f, 0.58823526f);
			GlStateManager.rotate(-25, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(0, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(135, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(0, -0.25f, -0.125f);
			GlStateManager.scale(0.5f, 0.5f, 0.5f);
			return true;
		}
		GlStateManager.scale(0.5f, 0.5f, 0.5f);
		return false;
	}

	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	@Override
	public String getDescription() {
		return "Changes the sword attack rendering to be that of 1.7.10 with the old block-hitting animations.";
	}
}
