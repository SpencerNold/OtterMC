package io.github.ottermc;

import agent.transformation.ClassAdapter;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.io.Secure;
import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.screen.hud.HudManager;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.transformers.EntityRendererTransformer;
import io.github.ottermc.transformers.GameSettingsTransformer;
import io.github.ottermc.transformers.GuiIngameTransformer;
import io.github.ottermc.transformers.MinecraftTransformer;

import java.io.File;

public class Client {

	public static final String NAME = "OtterMC", VERSION = "ALPHA-0.0.1 (1.8.9)", TARGET = "1.8.9";

	private static Client instance;

	private final KeybindManager keyManager = new KeybindManager();
	private final ModuleManager modManager = new ModuleManager();
	private final HudManager hudManager = new HudManager();
	private final ClientLogger errorManager = new ClientLogger();

	private final File clientDirectory;
	private final ClientStorage storage;

	public Client(File file, ClassAdapter adapter) {
		instance = this;
		this.clientDirectory = file;
		adapter.register(EntityRendererTransformer.class);
		adapter.register(GameSettingsTransformer.class);
		adapter.register(GuiIngameTransformer.class);
		adapter.register(MinecraftTransformer.class);
		storage = new ClientStorage(new File(file, "profile." + Secure.hash(TARGET)));
	}

	public void start() {
		registerEvents();
	}

	private void registerEvents() {
		EventBus.add(new InitializationManager());
		EventBus.add(keyManager);
		EventBus.add(new BlurShaderProgram());
		EventBus.add(hudManager);
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
	
	public static ClientLogger getErrorManager() {
		return instance.errorManager;
	}
}
