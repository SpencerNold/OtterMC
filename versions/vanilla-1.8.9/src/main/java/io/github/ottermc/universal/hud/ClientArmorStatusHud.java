package io.github.ottermc.universal.hud;

import io.github.ottermc.render.hud.impl.ArmorStatusHud;
import io.github.ottermc.screen.render.DrawableHelper;
import io.github.ottermc.universal.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ClientArmorStatusHud extends ArmorStatusHud {

    private static final ResourceLocation EMPTY_HELMET = new ResourceLocation("textures/items/empty_armor_slot_helmet.png");
    private static final ResourceLocation EMPTY_CHESTPLATE = new ResourceLocation("textures/items/empty_armor_slot_chestplate.png");
    private static final ResourceLocation EMPTY_LEGGINGS = new ResourceLocation("textures/items/empty_armor_slot_leggings.png");
    private static final ResourceLocation EMPTY_BOOTS = new ResourceLocation("textures/items/empty_armor_slot_boots.png");
    private static final ResourceLocation[] RESOURCES = new ResourceLocation[] { EMPTY_HELMET, EMPTY_CHESTPLATE, EMPTY_LEGGINGS, EMPTY_BOOTS };

    public ClientArmorStatusHud() {
        super(64, 16);
    }

    @Override
    public void draw(@Type(DrawableHelper.class) Object context) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) mc.getRenderViewEntity();
            ItemStack[] items = new ItemStack[4];
            for (int i = 0; i < 4; i++)
                items[i] = player.inventory.armorItemInSlot(3 - i);
            drawItems(items);
        }
    }

    @Override
    public void drawDummyObject(@Type(DrawableHelper.class) Object context) {
        ItemStack[] items = new ItemStack[]{
                new ItemStack(Items.diamond_helmet),
                new ItemStack(Items.diamond_chestplate),
                new ItemStack(Items.diamond_leggings),
                new ItemStack(Items.diamond_boots),
        };
        drawItems(items);
    }

    private void drawItems(ItemStack[] items) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        for (int i = 0; i < 4; i++) {
            int x = getDefaultX() + (i * 16);
            int y = getDefaultY();
            ItemStack stack = items[i];
            if (stack == null || stack.stackSize == 0) {
                mc.getTextureManager().bindTexture(RESOURCES[i]);
                Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
                continue;
            }
            RenderItem renderer = mc.getRenderItem();
            renderer.renderItemAndEffectIntoGUI(stack, x, y);
            renderer.renderItemOverlays(mc.fontRendererObj, stack, x, y);
        }
        GlStateManager.disableLighting();
    }
}
