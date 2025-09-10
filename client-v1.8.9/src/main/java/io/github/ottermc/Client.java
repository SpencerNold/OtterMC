package io.github.ottermc;

import agent.ClassTransformer;
import agent.ReflectionRequired;
import io.github.ottermc.api.Initializer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.keybind.LWJGLKeyboard;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.GameDisplay;
import io.github.ottermc.screen.hud.HudManager;
import io.github.ottermc.screen.hud.Movable;
import io.github.ottermc.screen.impl.MainMenuScreen;
import io.github.ottermc.screen.impl.MenuScreen;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.Icon;
import io.github.ottermc.transformers.EntityRendererTransformer;
import io.github.ottermc.transformers.GameSettingsTransformer;
import io.github.ottermc.transformers.GuiIngameTransformer;
import io.github.ottermc.transformers.MinecraftTransformer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Client implements Initializer {

	public static final String NAME = "OtterMC", VERSION = "ALPHA-1.0.0 (1.8.9)";
	@ReflectionRequired
	public static final String TARGET = "1.8.9";

	private static Client instance;

	private final KeybindManager keyManager = new KeybindManager();
	private final ModuleManager modManager = new ModuleManager();
	private final HudManager hudManager = new HudManager();
	private final ClientLogger errorManager = new ClientLogger();

	private final ClientStorage storage;
	private final File clientDirectory;


	@ReflectionRequired
	public Client(File file, ClassTransformer transformer) {
		instance = this;
		this.clientDirectory = file;
		this.storage = new ClientStorage(clientDirectory, String.join(" ", NAME, VERSION, TARGET));
		transformer.register(EntityRendererTransformer.class);
		transformer.register(GameSettingsTransformer.class);
		transformer.register(GuiIngameTransformer.class);
		transformer.register(MinecraftTransformer.class);
	}

	@ReflectionRequired
	public void start() {
		UniversalKeyboard.register(new LWJGLKeyboard());
		registerEvents();
	}

	@ReflectionRequired
	public void onPostInit() {
		Display.setTitle(Client.NAME + " " + Client.VERSION);
		Display.setIcon(new ByteBuffer[] { Icon.readIconToBuffer("otter_icon_16x16.png"), Icon.readIconToBuffer("otter_icon_32x32.png"), });
		registerGameHuds();
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.theWorld == null && mc.currentScreen != null && mc.currentScreen.getClass() == GuiMainMenu.class)
			mc.displayGuiScreen(new MainMenuScreen());
		errorManager.postInit();
	}

	@Override
	public void load() throws IOException {
		storage.clear();
		for (Module module : getModuleManager().getModules())
			storage.writable(module);
		for (Component component : getHudManager().getComponents()) {
			if (component instanceof Movable)
				storage.writable((Movable) component);
		}
		storage.read();
	}

	@Override
	public void save() throws IOException {
		storage.write();
	}

	private void registerEvents() {
		EventBus.add(new InitializationManager(this));
		EventBus.add(keyManager);
		EventBus.add(new BlurShaderProgram());
		EventBus.add(hudManager);
	}

	private void registerGameHuds() {
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
		hudManager.register(GameDisplay.TAB);
	}

	@Override
	public ModuleManager getModuleManager() {
		return modManager;
	}

	@Override
	public File getClientDirectory() {
		return clientDirectory;
	}

	public ClientStorage getStorage() {
		return storage;
	}

	public KeybindManager getKeyManager() {
		return instance.keyManager;
	}

	public HudManager getHudManager() {
		return instance.hudManager;
	}

	public ClientLogger getErrorManager() {
		return instance.errorManager;
	}

	@ReflectionRequired
	public static Client getInstance() {
		return instance;
	}
}
