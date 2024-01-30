package io.github.ottermc.modules.hypixel;

import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.world.World;

public abstract class HypixelModule extends Module {

	// TODO Future ideas?
	// Auto-tip
	// Level-head?
	// Some sort of win counter
	
	public HypixelModule(String name) {
		super(name, Category.HYPIXEL);
	}

	public final boolean isConnectedToHypixel() {
		Minecraft mc = Minecraft.getMinecraft();
		ServerData server = mc.getCurrentServerData();
		World world = mc.theWorld;
		return server != null && world != null && world.isRemote && server.serverIP.contains("hypixel.net");
	}
}
