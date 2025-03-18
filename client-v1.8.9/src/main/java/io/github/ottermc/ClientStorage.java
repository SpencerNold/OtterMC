package io.github.ottermc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.io.Secure;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.Writable;
import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.HudManager;
import io.github.ottermc.screen.hud.Movable;

public class ClientStorage {
	
	private static final int VERSION = (Client.NAME + Client.VERSION).hashCode();

	private final File file;
	
	private byte[] clientId = Secure.random(16);
	private final Map<Integer, Module> modules = new HashMap<>();
	private final Map<Integer, Storable<?>> storables = new HashMap<>();
	private final Map<Integer, Component> components = new HashMap<>();
	
	private boolean initialized;
	
	public ClientStorage(File file) {
		this.file = file;
	}
	
	public void init() {
		ModuleManager modManager = Client.getModManager();
		modManager.forEach(mod -> {
			int id = mod.getSerialId();
			modules.put(id, mod);
			Writable<ByteBuf>[] writables = mod.getWritables();
			if (writables == null)
				return;
			for (Writable<ByteBuf> writable : writables) {
				if (!(writable instanceof Storable<?>))
					continue;
				Storable<?> storable = (Storable<?>) writable;
				storables.put(storable.getSerialId() + id, storable);
			}
		});
		HudManager hudManager = Client.getHudManager();
		hudManager.filter(component -> component instanceof Movable).forEach(component -> {
			components.put(((Movable) component).getSerialId(), component);
		});
		initialized = true;
	}
	
	public void read() throws IOException {
		if (!initialized)
			return;
		if (!file.exists())
			return;
		byte[] bytes = Files.readAllBytes(file.toPath());
		ByteBuf buf = new ByteBuf(bytes);
		buf.readInt(); // TODO reserved for future use
		clientId = buf.read(16);
		int n = buf.readShort();
		int i;
		for (i = 0; i < n; i++) {
			int id = buf.readInt();
			boolean active = buf.readBoolean();
			if (modules.containsKey(id))
				modules.get(id).setActive(active);
		}
		n = buf.readShort();
		for (i = 0; i < n; i++) {
			int id = buf.readInt();
			if (storables.containsKey(id))
				storables.get(id).read(buf);
		}
		n = buf.readShort();
		for (i = 0; i < n; i++) {
			int id = buf.readInt();
			if (components.containsKey(id))
				((Movable) components.get(id)).read(buf);
		}
	}
	
	public void write() throws IOException {
		if (!initialized)
			return;
		if (!file.exists()) {
			if (!file.createNewFile())
				return;
		}
		ByteBuf buf = new ByteBuf();
		buf.writeInt(VERSION);
		buf.write(clientId);
		buf.writeShort((short) modules.size());
		modules.forEach((id, mod) -> {
			buf.writeInt(id);
			buf.writeBoolean(mod.isActive());
		});
		buf.writeShort((short) storables.size());
		storables.forEach((id, storable) -> {
			buf.writeInt(id);
			storable.write(buf);
		});
		buf.writeShort((short) components.size());
		components.forEach((id, component) -> {
			buf.writeInt(id);
			((Movable) component).write(buf);
		});
		Files.write(file.toPath(), buf.getDataBuffer());
	}
	
	public byte[] getClientId() {
		return clientId;
	}
}
