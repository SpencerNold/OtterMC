package io.github.ottermc.pvp.screen.hud.client;

import io.github.ottermc.screen.hud.MovableComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ClientArmorStatusHud extends MovableComponent {

    private static final ResourceLocation EMPTY_HELMET = new ResourceLocation("textures/items/empty_armor_slot_helmet.png");
    private static final ResourceLocation EMPTY_CHESTPLATE = new ResourceLocation("textures/items/empty_armor_slot_chestplate.png");
    private static final ResourceLocation EMPTY_LEGGINGS = new ResourceLocation("textures/items/empty_armor_slot_leggings.png");
    private static final ResourceLocation EMPTY_BOOTS = new ResourceLocation("textures/items/empty_armor_slot_boots.png");

    public ClientArmorStatusHud() {
        super(10, 10, 64, 16);
    }

    @Override
    protected void draw(Minecraft mc, GuiIngame gui, float partialTicks) {
        if (mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) mc.getRenderViewEntity();
            ItemStack[] items = new ItemStack[4];
            for (int i = 0; i < 4; i++)
                items[i] = player.inventory.armorItemInSlot(3 - i);
            drawItems(items);
        }
    }

    @Override
    public void drawDummyObject(Minecraft mc, Gui gui, float partialTicks) {
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
            ItemStack stack = items[i];
            if (stack == null) {
                switch (i) {
                    case 0:
                        mc.getTextureManager().bindTexture(EMPTY_HELMET);
                        break;
                    case 1:
                        mc.getTextureManager().bindTexture(EMPTY_CHESTPLATE);
                        break;
                    case 2:
                        mc.getTextureManager().bindTexture(EMPTY_LEGGINGS);
                        break;
                    case 3:
                        mc.getTextureManager().bindTexture(EMPTY_BOOTS);
                        break;
                }
                Gui.drawModalRectWithCustomSizedTexture(getDefaultX() + (i * 16), getDefaultY(), 0, 0, 16, 16, 16, 16);
                continue;
            }
            RenderItem renderer = mc.getRenderItem();
            renderer.renderItemAndEffectIntoGUI(stack, getDefaultX() + (i * 16), getDefaultY());
            renderer.renderItemOverlays(mc.fontRendererObj, stack, getDefaultX() + (i * 16), getDefaultY());
        }
        GlStateManager.disableLighting();
    }

    @Override
    public int getSerialId() {
        return "ARMOR_STATUS_COMPONENT".hashCode();
    }
}
