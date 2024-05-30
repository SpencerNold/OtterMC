package io.github.ottermc;

import agent.transformation.ClassAdapter;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.io.ClientStorage;
import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.modules.analytical.Analytical;
import io.github.ottermc.modules.analytical.AnalyticalAPI;
import io.github.ottermc.modules.hud.*;
import io.github.ottermc.modules.hypixel.AutoGG;
import io.github.ottermc.modules.hypixel.GameMacro;
import io.github.ottermc.modules.utility.Fullbright;
import io.github.ottermc.modules.utility.Zoom;
import io.github.ottermc.modules.visual.*;
import io.github.ottermc.screen.hud.ClientDisplay;
import io.github.ottermc.screen.hud.GameDisplay;
import io.github.ottermc.screen.hud.HudManager;
import io.github.ottermc.screen.impl.GameMacroScreen;
import io.github.ottermc.screen.impl.MenuScreen;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.transformers.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.io.File;

public class Client {

	public static final String NAME = "OtterMC", VERSION = "ALPHA-0.0.1";

	private static Client instance;

	private final KeybindManager keyManager = new KeybindManager();
	private final ModuleManager modManager = new ModuleManager();
	private final HudManager hudManager = new HudManager();
	private final ClientLogger errorManager = new ClientLogger();

	private final File clientDirectory;
	private final ClientStorage storage;
	private final AnalyticalAPI analyticalAPI;

	public Client(File file, ClassAdapter adapter) {
		instance = this;
		this.clientDirectory = file;
		adapter.register(EntityPlayerSPTransformer.class);
		adapter.register(EntityRendererTransformer.class);
		adapter.register(EntityTransformer.class);
		adapter.register(GameSettingsTransformer.class);
		adapter.register(GuiIngameTransformer.class);
		adapter.register(GuiScreenTransformer.class);
		adapter.register(ItemRendererTransformer.class);
		adapter.register(LayerArmorBaseTransformer.class);
		adapter.register(MinecraftTransformer.class);
		adapter.register(PlayerControllerMPTransformer.class);
		adapter.register(RenderEntityItemTransformer.class);
		adapter.register(RenderGlobalTransformer.class);
		adapter.register(RenderItemTransformer.class);
		storage = new ClientStorage(new File(file, "profile.data"));
		analyticalAPI = new AnalyticalAPI("insert_link_here: very much incomplete...");
	}

	public void start() {
		registerEvents();
		registerKeybinds();
	}

	private void registerEvents() {
		EventBus.add(new InitializationManager(this));
		EventBus.add(keyManager);
		EventBus.add(new BlurShaderProgram());
		EventBus.add(hudManager);
	}

	private void registerKeybinds() {
		keyManager.register(Keyboard.KEY_RSHIFT, () -> {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.currentScreen == null)
				mc.displayGuiScreen(new MenuScreen());
		});
		keyManager.register(Keyboard.KEY_M, () -> {
			if (GameMacro.isModActive()) {
				Minecraft mc = Minecraft.getMinecraft();
				if (mc.currentScreen == null)
					mc.displayGuiScreen(new GameMacroScreen());
			}
		});
	}

	void registerModules() {
		// HUD
		modManager.register(new ArmorStatus());
		modManager.register(new Array());
		modManager.register(new ClickCounter());
		modManager.register(new Coordinate());
		modManager.register(new Direction());
		modManager.register(new GuiBlur());
		modManager.register(new KeyStroke());
		modManager.register(new PotionEffect());

		// Utility
//		modManager.register(new Chat()); TODO Remove instance null check in this class when finished
		modManager.register(new Fullbright());
		modManager.register(new Zoom());

		// Visual
		modManager.register(new BlockOutline());
		modManager.register(new ColorTheme());
		modManager.register(new DamageColor());
		modManager.register(new EnchantmentGlint());
		modManager.register(new LargeItems());
		modManager.register(new OldAnimation());

		// Hypixel
		modManager.register(new AutoGG());
		modManager.register(new GameMacro());

		// Analytical
		modManager.register(new Analytical());
	}

	void registerHuds() {
		// Default Minecraft HUD
		hudManager.register(GameDisplay.PUMPKIN_OVERLAY);
		hudManager.register(GameDisplay.NAUSEA_EFFECT);
		hudManager.register(GameDisplay.TOOLTIP);
		hudManager.register(GameDisplay.BOSS_BAR);
		hudManager.register(GameDisplay.PLAYER_STATS);
		hudManager.register(GameDisplay.SLEEP_MENU);
		hudManager.register(GameDisplay.EXP_BAR);
		hudManager.register(GameDisplay.OVERLAY_TEXT);
		hudManager.register(GameDisplay.SCOREBOARD);
		hudManager.register(GameDisplay.TITLE);
		hudManager.register(GameDisplay.CHAT);
		hudManager.register(GameDisplay.TAB);
		// Client HUD
		hudManager.register(ClientDisplay.ARMOR_STATUS);
		hudManager.register(ClientDisplay.ARRAY);
		hudManager.register(ClientDisplay.CLICK_COUNTER);
		hudManager.register(ClientDisplay.COORDINATE);
		hudManager.register(ClientDisplay.DIRECTION);
		hudManager.register(ClientDisplay.KEYSTROKE);
		hudManager.register(ClientDisplay.POTION_EFFECT);

		ClientLogger.display(ClientLogger.Severity.ERROR, "Test");
	}

	public static KeybindManager getKeyManager() {
		return instance.keyManager;
	}

	public static ModuleManager getModManager() {
		return instance.modManager;
	}

	public static HudManager getHudManager() {
		return instance.hudManager;
	}

	public static ClientStorage getClientStorage() {
		return instance.storage;
	}

	public static File getClientDirectory() {
		return instance.clientDirectory;
	}
	
	public static AnalyticalAPI getAnalyticalAPI() {
		return instance.analyticalAPI;
	}

	public static ClientLogger getErrorManager() {
		return instance.errorManager;
	}
}
