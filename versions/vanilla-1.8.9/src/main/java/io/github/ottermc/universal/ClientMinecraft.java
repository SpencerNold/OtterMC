package io.github.ottermc.universal;

import net.minecraft.client.Minecraft;

public class ClientMinecraft extends UMinecraft {

    @Override
    protected Object _getCurrentScreen() {
        return getMinecraft().currentScreen;
    }

    private Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
}
