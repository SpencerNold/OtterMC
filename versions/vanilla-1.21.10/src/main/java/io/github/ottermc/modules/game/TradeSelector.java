package io.github.ottermc.modules.game;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RenderGameOverlayListener;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.modules.CategoryList;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TradeSelector extends TradeModule implements RunTickListener, RenderGameOverlayListener {

    private final List<Trade> targettedBookTrades = new ArrayList<>();

    private int state = 4;

    private boolean villager = false;
    private boolean workstation = false;

    public TradeSelector() {
        super("Trade Selector", CategoryList.GAME);
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
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
    }

    @Override
    public void onRunTick(RunTickEvent event) {
        /*
        villager = false;
        MinecraftClient client = MinecraftClient.getInstance();
        BlockPos lecternPos = findLectern();
        workstation = lecternPos != null;
        if (client.player == null)
            return;
        if (client.interactionManager == null)
            return;
        if (client.targetedEntity == null)
            return;
        if (client.targetedEntity.getType() != EntityType.VILLAGER)
            return;
        villager = true;
        if (lecternPos == null)
            return;
        if (state == 0) {
            if (client.currentScreen != null)
                return;
            client.interactionManager.interactEntity(client.player, client.targetedEntity, Hand.MAIN_HAND);
            client.player.swingHand(Hand.MAIN_HAND);
            state = 1;
        } else if (state == 1) {
            if (!(client.currentScreen instanceof MerchantScreen)) {
                state = 0;
                return;
            }
            MerchantScreen screen = (MerchantScreen) client.currentScreen;
            TradeOfferList trades = ((MerchantScreenHandler) ((ScreenHandlerProvider<ScreenHandler>) ((HandledScreen) screen)).getScreenHandler()).getRecipes();
            for (TradeOffer offer : trades) {
                ItemStack stack = offer.getSellItem();
                if (stack.getItem() != Items.ENCHANTED_BOOK)
                    continue;
                Set<Object2IntMap.Entry<RegistryEntry<Enchantment>>> enchantments = EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries();
                if (enchantments.isEmpty())
                    continue;
                Object2IntMap.Entry<RegistryEntry<Enchantment>> first = enchantments.stream().findFirst().orElseThrow();
                String name = first.getKey().getIdAsString();
                int level = first.getIntValue();
                int cost = offer.getFirstBuyItem().count();
                boolean has = false;
                for (Trade trade : targettedBookTrades) {
                    if (trade.target.equals(name + level) && cost <= trade.max) {
                        has = true;
                        break;
                    }
                }
                if (has)
                    state = 4;
                else {
                    state = 2;
                    client.player.closeHandledScreen();
                }
            }
        } else if (state == 2) {
            if (client.world.getBlockState(lecternPos).getBlock() == Blocks.AIR) {
                state = 3;
                return;
            }
            int slot = -1;
            int type = -1;
            // lazy but effective tool selection algorithm
            for (int i = 0; i < 9; i++) {
                ItemStack stack = ((PlayerEntity) client.player).getInventory().getStack(i);
                if (stack.getItem() == Items.NETHERITE_AXE) {
                    if (type < 3) {
                        slot = i;
                        type = 3;
                    }
                } else if (stack.getItem() == Items.DIAMOND_AXE) {
                    if (type < 2) {
                        slot = i;
                        type = 2;
                    }
                } else if (stack.getItem() == Items.IRON_AXE) {
                    if (type < 1) {
                        slot = i;
                        type = 1;
                    }
                } else if (stack.getItem() == Items.STONE_AXE) {
                    if (type < 0) {
                        slot = i;
                        type = 0;
                    }
                }
            }
            if (slot != -1)
                ((PlayerEntity) client.player).getInventory().setSelectedSlot(slot);
            if (client.interactionManager.updateBlockBreakingProgress(lecternPos, Direction.UP))
                client.player.swingHand(Hand.MAIN_HAND);
        } else if (state == 3) {
            System.out.println("State 3!");
        }
        */
    }

    private BlockPos findLectern() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null)
            return null;
        int xPos = (int) ((Entity) client.player).getX();
        int yPos = (int) ((Entity) client.player).getY();
        int zPos = (int) ((Entity) client.player).getZ();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    BlockPos pos = new BlockPos(xPos + x - 1, xPos + y - 1, zPos + z - 1);
                    if (((World) client.world).getBlockState(pos).getBlock() == Blocks.LECTERN)
                        return pos;
                }
            }
        }
        return null;
    }
}
