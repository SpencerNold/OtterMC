package io.github.ottermc;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.modules.hud.GuiBlur;
import io.github.ottermc.modules.impl.display.UIScheme;
import io.github.ottermc.modules.visual.*;
import io.github.ottermc.render.hud.HudManager;
import io.github.ottermc.render.screen.Charset;
import io.github.ottermc.render.screen.UniversalFontRenderer;
import io.github.ottermc.screen.font.ClientFont;
import io.github.ottermc.screen.font.FontRenderer;
import io.github.ottermc.screen.impl.MainMenuScreen;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.Icon;
import io.github.ottermc.transformer.TransformerRegistry;
import io.github.ottermc.transformers.*;
import io.github.ottermc.universal.Math;
import io.github.ottermc.universal.*;
import io.github.ottermc.universal.hud.*;
import io.ottermc.transformer.ReflectionRequired;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.nio.ByteBuffer;

public class SubClient extends AbstractSubClient {

    public static final String NAME = "OtterMC", VERSION = "ALPHA-1.0.0";
    public static final String TARGET = "1.8.9";

    private static SubClient instance;

    private final KeybindManager keyManager = new KeybindManager();
    private final ModuleManager modManager = new ModuleManager();
    private final HudManager hudManager = new ClientHudManager();

    private final File clientDirectory;


    @ReflectionRequired
    public SubClient(File file, TransformerRegistry registry) {
        registerBindings();
        instance = this;
        this.clientDirectory = file;
        registerTransformers(registry);

        registry.registerPost(RendererLivingEntityTransformer.class);
    }

    @Override
    @ReflectionRequired
    public void start() {
        registerEvents();
    }

    @Override
    @ReflectionRequired
    public void onPostInit() {
        UniversalFontRenderer.register(new FontRenderer(ClientFont.getFontIgnoreException("/assets/omc/omc_ttf_font.png"), new Charset("/assets/omc/omc_ttf_charset.json")));
        registerDisplays();
        registerModules();
        Display.setTitle(SubClient.NAME + " " + SubClient.VERSION);
        Display.setIcon(new ByteBuffer[]{Icon.readIconToBuffer("otter_icon_16x16.png"), Icon.readIconToBuffer("otter_icon_32x32.png"),});
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null && mc.currentScreen != null && mc.currentScreen.getClass() == GuiMainMenu.class)
            mc.displayGuiScreen(new MainMenuScreen());
    }

    private void registerBindings() {
        UniversalLog4j.register(new Log4j());
        UKeyboard.register(new LWJGLKeyboard());
        UDrawable.register(new ClientDrawable());
        UGameSettings.register(new ClientGameSettings());
        UKeyRegistry.register(new ClientKeyRegistry());
        UMinecraft.register(new ClientMinecraft());
        UVersion.register(new ClientVersion());
        Mth.register(new Math());
        UPlayer.register(new ClientPlayer());
    }

    private void registerTransformers(TransformerRegistry registry) {
        registry.register(EntityRendererTransformer.class);
        registry.register(GameSettingsTransformer.class);
        registry.register(GuiIngameTransformer.class);
        registry.register(EntityPlayerSPTransformer.class);
        registry.register(EntityTransformer.class);
        registry.register(GuiScreenTransformer.class);
        registry.register(ItemRendererTransformer.class);
        registry.register(LayerArmorBaseTransformer.class);
        registry.register(MinecraftTransformer.class);
        registry.register(PlayerControllerMPTransformer.class);
        registry.register(RenderEntityItemTransformer.class);
        registry.register(RenderGlobalTransformer.class);
        registry.register(RenderItemTransformer.class);
    }

    private void registerDisplays() {
        hudManager.register(new ClientArmorStatusHud());
        hudManager.register(new ClientPotionEffectHud());
        hudManager.register(new ClientKeyStrokeHud());
        hudManager.register(new ClientCoordinateHud());
        hudManager.register(new ClientClickCounterHud());
    }

    private void registerEvents() {
        EventBus.add(new BlurShaderProgram());
    }

    private void registerModules() {
        // HUD
        modManager.register(new GuiBlur());

        // Visual
        modManager.register(new BlockOutline());
        modManager.register(new DamageColor());
        modManager.register(new EnchantmentGlint());
        modManager.register(new LargeItems());
        modManager.register(new OldAnimation());
        modManager.register(new UIScheme());
    }

    @Override
    public ModuleManager getModuleManager() {
        return modManager;
    }

    @Override
    public File getClientDirectory() {
        return clientDirectory;
    }

    @Override
    public KeybindManager getKeybindManager() {
        return instance.keyManager;
    }

    @Override
    public HudManager getHudManager() {
        return instance.hudManager;
    }

    @Override
    public String getIdentifier() {
        return String.join("%s (%s) v%s", NAME, TARGET, VERSION);
    }

    @ReflectionRequired
    public static SubClient getInstance() {
        return instance;
    }
}
