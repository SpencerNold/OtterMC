package io.github.ottermc.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import io.github.ottermc.Client;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.modules.settings.Storable;
import io.github.ottermc.modules.settings.Writable;

public class ClientStorage {
	
	private static final int VERSION = (Client.NAME + Client.VERSION).hashCode();

	private final File file;
	
	private byte[] clientId = Secure.random(16);
	private final Map<Integer, Module> modules = new HashMap<>();
	private final Map<Integer, Storable<?>> storables = new HashMap<>();
	
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
			modules.get(id).setActive(active);
		}
		n = buf.readShort();
		for (i = 0; i < n; i++) {
			int id = buf.readInt();
			storables.get(id).read(buf);
		}
	}
	
	public void write() throws IOException {
		if (!initialized)
			return;
		if (!file.exists())
			file.createNewFile();
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
		Files.write(file.toPath(), buf.getDataBuffer());
	}
}
