package io.github.ottermc.modules.world;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.universal.UKeyboard;
import io.github.ottermc.listeners.DrawOverlayListener;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.BooleanSetting;
import io.github.ottermc.modules.setting.KeyboardSetting;
import io.github.ottermc.modules.setting.StringSetting;
import io.github.ottermc.modules.CategoryList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class BiomeFinder extends Module implements RunTickListener, DrawOverlayListener {

    private static final Identifier ID = Identifier.of("omc", "omc_ttf_font.png");

    private final StringSetting seedSetting = new StringSetting("Seed", "0", 1);
    private final BooleanSetting largeBiomesSetting = new BooleanSetting("Large Biomes", false);
    private final KeyboardSetting mapKeyboardSetting = new KeyboardSetting("Map Keybind", GLFW.GLFW_KEY_B);

    private int[] cells = new int[0];
    private int rows = 0, cols = 0;
    private int offsX = 0, offsY = 0;

    public BiomeFinder() {
        super("Biome Finder", CategoryList.WORLD);
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
    public void onRunTick(RunTickEvent event) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null)
            return;
        int width = client.getWindow().getScaledWidth() - 40;
        int height = client.getWindow().getScaledHeight() - 40;
        int rows = MathHelper.floor(height / 16.0d);
        int cols = MathHelper.floor(width / 16.0d);
        if (this.rows != rows || this.cols != cols) {
            this.rows = rows;
            this.cols = cols;
            this.offsX = (width + 40 - (cols * 16)) / 2;
            this.offsY = (height + 40 - (rows * 16)) / 2;
            this.cells = new int[rows * cols];

            int x = 0, y = 0, z = 0;
            long seed = 0L;

            //World world = client.world;
            //DynamicRegistryManager registries = world.getRegistryManager();

            //ChunkGenerator generator = new NoiseChunkGenerator(biomeSource, noiseSettings);
            //NoiseConfig config = NoiseConfig.create(generator, registries, seed);
            //MultiNoiseUtil.MultiNoiseSampler sampler = config.getMultiNoiseSampler();
            //int bx = x >> 2, by = y >> 2, bz = z >> 2;
            //RegistryEntry<Biome> biome = generator.getBiomeSource().getBiome(bx, by, bx, sampler);

            Arrays.fill(cells, 0xFFF509F5);
        }
    }

    @Override
    public void onDrawOverlay(DrawOverlayEvent event) {
        if (!UKeyboard.isKeyDown(mapKeyboardSetting.getValue()))
            return;
        MinecraftClient client = MinecraftClient.getInstance();
        DrawContext context = event.getContext();
        int width = cols * 16;
        int height = rows * 16;
        // Draw cells
        for (int i = 0; i < cells.length; i++) {
            int color = cells[i];
            int x = i % cols;
            int y = i / cols;
            int realX = offsX + x * 16;
            int realY = offsY + y * 16;
            context.fill(realX, realY, realX + 16, realY + 16, color);
        }

        // Draw axis
        context.drawHorizontalLine(offsX, offsX + width - 1, offsY + (height / 2), -1);
        context.drawVerticalLine(offsX + (width / 2), offsY - 1, offsY + height, -1);

        // Draw text
        context.drawTexturedQuad(ID, 0, 0, 0, 0, 100, 100, 100, 100);
    }

    @Override
    public Storable<?>[] getWritables() {
        return new Storable<?>[]{seedSetting, largeBiomesSetting, mapKeyboardSetting};
    }
}
