package io.github.ottermc;

import io.github.ottermc.keybind.UniversalKeyboard;
import io.ottermc.transformer.ReflectionRequired;
import io.github.ottermc.api.Initializer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.keybind.LWJGLKeyboard;
import io.github.ottermc.logging.UniversalLog4j;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.HudManager;
import io.github.ottermc.screen.hud.MovableComponent;
import io.github.ottermc.screen.impl.EditHudScreen;
import io.github.ottermc.screen.impl.MainMenuScreen;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.Icon;
import io.github.ottermc.tools.Log4j;
import io.github.ottermc.transformers.EntityRendererTransformer;
import io.github.ottermc.transformers.GameSettingsTransformer;
import io.github.ottermc.transformers.GuiIngameTransformer;
import io.github.ottermc.transformers.MinecraftTransformer;
import io.ottermc.transformer.TransformerRegistry;
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

	private final ClientStorage storage;
	private final File clientDirectory;


	@ReflectionRequired
	public Client(File file, TransformerRegistry registry) {
		UniversalLog4j.register(new Log4j());
		instance = this;
		this.clientDirectory = file;
		this.storage = new ClientStorage(clientDirectory, String.join(" ", NAME, VERSION, TARGET));
		registry.register(EntityRendererTransformer.class);
		registry.register(GameSettingsTransformer.class);
		registry.register(GuiIngameTransformer.class);
		registry.register(MinecraftTransformer.class);
	}

	@ReflectionRequired
	public void start() {
		UniversalKeyboard.register(new LWJGLKeyboard());
		registerEvents();
	}

	@ReflectionRequired
	public void onPostInit() {
		registerKeybinds();
		Display.setTitle(Client.NAME + " " + Client.VERSION);
		Display.setIcon(new ByteBuffer[] {Icon.readIconToBuffer("otter_icon_16x16.png"), Icon.readIconToBuffer("otter_icon_32x32.png"), });
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.theWorld == null && mc.currentScreen != null && mc.currentScreen.getClass() == GuiMainMenu.class)
			mc.displayGuiScreen(new MainMenuScreen());
	}

	@Override
	public void load() throws IOException {
		storage.clear();
		for (Module module : getModuleManager().getModules())
			storage.writable(module);
		for (Component component : getHudManager().getComponents()) {
			if (component instanceof MovableComponent)
				storage.writable((MovableComponent) component);
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

	private void registerKeybinds() {
		KeybindManager manager = Client.getInstance().getKeybindManager();
		manager.register(Keyboard.KEY_RSHIFT, () -> {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.thePlayer != null && mc.currentScreen == null)
				mc.displayGuiScreen(new EditHudScreen());
		});
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

	public KeybindManager getKeybindManager() {
		return instance.keyManager;
	}

	public HudManager getHudManager() {
		return instance.hudManager;
	}

	@ReflectionRequired
	public static Client getInstance() {
		return instance;
	}
}
