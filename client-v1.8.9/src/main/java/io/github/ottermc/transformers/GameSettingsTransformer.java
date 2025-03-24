package io.github.ottermc.transformers;

import io.github.ottermc.Client;
import me.spencernold.transformer.*;
import net.minecraft.client.settings.GameSettings;

import java.io.File;

@Transformer(className = "net/minecraft/client/settings/GameSettings")
public class GameSettingsTransformer {
	
	@Injector(name = "loadOptions()V", target = Target.HEAD)
	public void onLoadOptionsHead(GameSettings settings, Callback callback) {
		File file = (File) Reflection.getValue(GameSettings.class, settings, "optionsFile");
		File save = new File(Client.getClientDirectory(), "ottermc-client-settings.txt");
		if (!file.exists())
			return;
		if (!save.exists())
			return;
		Reflection.setValue(GameSettings.class, settings, "optionsFile", save);
	}
	
	@Injector(name = "loadOptions()V", target = Target.TAIL)
	public void onLoadOptionsTail(GameSettings settings, Callback callback) {
		File save = new File(Client.getClientDirectory(), "ottermc-client-settings.txt");
		Reflection.setValue(GameSettings.class, settings, "optionsFile", save);
	}
}
