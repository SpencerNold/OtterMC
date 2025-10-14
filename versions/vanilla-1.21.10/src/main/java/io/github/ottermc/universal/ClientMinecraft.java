package io.github.ottermc.universal;

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
