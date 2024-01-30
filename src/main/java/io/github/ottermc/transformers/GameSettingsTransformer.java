package io.github.ottermc.transformers;

import java.io.File;

import agent.Callback;
import agent.Injector;
import agent.Reflection;
import agent.Target;
import agent.Transformer;
import io.github.ottermc.Client;
import net.minecraft.client.settings.GameSettings;

@Transformer(name = "net/minecraft/client/settings/GameSettings")
public class GameSettingsTransformer {
	
	@Injector(name = "loadOptions()V", target = Target.HEAD)
	public void onLoadOptionsHead(GameSettings settings, Callback callback) {
		File file = (File) Reflection.getMinecraftField("net/minecraft/client/settings/GameSettings", "optionsFile", settings);
		File save = new File(Client.getClientDirectory(), "ottermc-client-settings.txt");
		if (!file.exists())
			return;
		if (!save.exists())
			return;
		Reflection.setMinecraftField("net/minecraft/client/settings/GameSettings", "optionsFile", settings, save);
	}
	
	@Injector(name = "loadOptions()V", target = Target.TAIL)
	public void onLoadOptionsTail(GameSettings settings, Callback callback) {
		File save = new File(Client.getClientDirectory(), "ottermc-client-settings.txt");
		Reflection.setMinecraftField("net/minecraft/client/settings/GameSettings", "optionsFile", settings, save);
	}
}
