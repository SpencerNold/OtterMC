package ottermc.v1218;

import io.github.ottermc.render.screen.AbstractScreen;
import net.minecraft.client.MinecraftClient;

public class ClientDrawable extends io.github.fabric.universal.ClientDrawable {

    @Override
    protected void _display(AbstractScreen screen) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && mc.currentScreen == null)
            mc.setScreen(new ClientAbstractScreen(screen));
    }
}
