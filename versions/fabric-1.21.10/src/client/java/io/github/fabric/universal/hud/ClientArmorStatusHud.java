package io.github.fabric.universal.hud;

import io.github.ottermc.render.hud.impl.ArmorStatusHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ClientArmorStatusHud extends ArmorStatusHud {

    private static final Identifier EMPTY_HELMET = Identifier.ofVanilla("textures/gui/sprites/container/slot/helmet.png");
    private static final Identifier EMPTY_CHESTPLATE = Identifier.ofVanilla("textures/gui/sprites/container/slot/chestplate.png");
    private static final Identifier EMPTY_LEGGINGS = Identifier.ofVanilla("textures/gui/sprites/container/slot/leggings.png");
    private static final Identifier EMPTY_BOOTS = Identifier.ofVanilla("textures/gui/sprites/container/slot/boots.png");
    private static final Identifier EMPTY_SHIELD = Identifier.ofVanilla("textures/gui/sprites/container/slot/shield.png");
    private static final Identifier[] RESOURCES = new Identifier[] { EMPTY_HELMET, EMPTY_CHESTPLATE, EMPTY_LEGGINGS, EMPTY_BOOTS, EMPTY_SHIELD };

    public ClientArmorStatusHud() {
        super(80, 16);
    }

    @Override
    public void draw(Object context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getCameraEntity() instanceof ClientPlayerEntity) {
            LivingEntity player = (LivingEntity) mc.getCameraEntity();
            ItemStack[] items = new ItemStack[]{
                    player.getEquippedStack(EquipmentSlot.HEAD),
                    player.getEquippedStack(EquipmentSlot.CHEST),
                    player.getEquippedStack(EquipmentSlot.LEGS),
                    player.getEquippedStack(EquipmentSlot.FEET),
                    player.getEquippedStack(EquipmentSlot.OFFHAND)
            };
            drawItems((DrawContext) context, items);
        }
    }

    @Override
    public void drawDummyObject(Object context) {
        ItemStack[] items = new ItemStack[]{
                new ItemStack(Items.DIAMOND_HELMET),
                new ItemStack(Items.DIAMOND_CHESTPLATE),
                new ItemStack(Items.DIAMOND_LEGGINGS),
                new ItemStack(Items.DIAMOND_BOOTS),
                new ItemStack(Items.SHIELD)
        };
        drawItems((DrawContext) context, items);
    }

    private void drawItems(DrawContext context, ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            int x = getDefaultX() + (i * 16);
            int y = getDefaultY();
            ItemStack stack = items[i];
            if (stack == null || stack.isEmpty()) {
                Identifier id = RESOURCES[i];
                context.drawTexturedQuad(id, x, y, x + 16, y + 16, 0, 1, 0, 1);
                continue;
            }
            context.drawItem(stack, x, y);
            context.drawStackOverlay(MinecraftClient.getInstance().textRenderer, stack, x, y);
        }
    }
}
