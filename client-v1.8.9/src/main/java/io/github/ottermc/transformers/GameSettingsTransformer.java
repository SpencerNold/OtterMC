package io.github.ottermc.transformers;

import agent.Reflection;
import io.github.ottermc.Client;
import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import net.minecraft.client.settings.GameSettings;

import java.io.File;

@Transformer(className = "net/minecraft/client/settings/GameSettings")
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
