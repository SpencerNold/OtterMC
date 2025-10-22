package io.github.fabric.universal;

import io.github.ottermc.universal.UMinecraft;
import net.minecraft.client.MinecraftClient;

public class ClientMinecraft extends UMinecraft {

    @Override
    protected Object _getCurrentScreen() {
        return getMinecraft().currentScreen;
    }

    private MinecraftClient getMinecraft() {
        return MinecraftClient.getInstance();
    }
}
