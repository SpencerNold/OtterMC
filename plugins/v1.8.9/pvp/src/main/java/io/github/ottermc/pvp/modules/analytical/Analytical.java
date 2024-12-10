package io.github.ottermc.pvp.modules.analytical;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.AttackEntityListener;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.listeners.SaveGameListener;
import io.github.ottermc.pvp.listeners.SetVelocityListener;
import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Analytical extends Module implements AttackEntityListener, SetVelocityListener, RunTickListener, SaveGameListener {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/clipboard_icon.png");

	private static Analytical instance;
	
	// increase size as helpers are added
	private final StatHelper[] helpers = new StatHelper[6];
	
	private boolean status;

	private final AnalyticalAPI api = new AnalyticalAPI("");
	
	public Analytical() {
		super("Analytical Data", Category.ANALYTICAL);
		instance = this;
		setActive(true);
		for (int i = 0; i < helpers.length; i++)
			helpers[i] = new StatHelper(i);
		api.checkConnection().thenAccept(b -> {
			status = b;
		});
	}
	
	@Override
	public void onEnable() {
		EventBus.add(this);
	}
	
	@Override
	public void onDisable() {
		EventBus.remove(this);
	}
	
	@Override
	public void onRunTick(RunTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		ServerData currentServerData = mc.getCurrentServerData();
		World world = mc.theWorld;
		if (currentServerData != null && world != null && world.isRemote) {
			int ping = MathHelper.clamp_int((int) currentServerData.pingToServer, 0, 5000); // If you're ping is 5000ms, you probably are n
			helpers[0].add(ping);
		}
	}
	
	@Override
	public void onAttackEntity(AttackEntityEvent event) {
		PlayerControllerMP controller = (PlayerControllerMP) event.getController();
		if (controller.extendedReach())
			return;
		EntityPlayer player = (EntityPlayer) event.getPlayer();
		Entity entity = (Entity) event.getEntity();
		float distance = ((Entity) player).getDistanceToEntity(entity);
		helpers[1].add(distance);
		if (distance > 3.0f) {
			float extend = distance - 3.0f;
			helpers[2].add(extend);
		}
	}
	
	@Override
	public void onSetVelocity(SetVelocityEvent event) {
		if (event.getEntity() instanceof EntityPlayerSP) {
			double x = event.getX(), y = event.getY(), z = event.getZ();
			helpers[3].add((float) x);
			helpers[4].add((float) y);
			helpers[5].add((float) z);
		}
	}
	
	@Override
	public void onSaveGame(SaveGameEvent event) {
		if (status) {
			for (StatHelper helper : helpers)
				api.sendData(helper);
		}
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
	
	@Override
	public String getDescription() {
		return "Anonymously sends analytical data to the developers. Visit our website to see what we collect and how we do it.";
	}
	
	public static boolean isModActive() {
		return instance.isActive();
	}
}
