package io.github.ottermc;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.io.Secure;
import io.github.ottermc.modules.Writable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;

public class ClientStorage {

    public static final int FILE_VERSION = 0;

    private final File file;
    private byte[] clientId = Secure.random(32);

    private final ArrayList<Writable<ByteBuf>> writables = new ArrayList<>();

    public ClientStorage(File directory, String identifier) {
        String name = "profile." + Secure.hashToString(identifier.getBytes(StandardCharsets.UTF_8), Secure.BASE64_TRANSFORMER);
        this.file = new File(directory, name);
    }

    public void read() throws IOException {
        if (!file.exists())
            return;
        byte[] bytes = Files.readAllBytes(file.toPath());
        ByteBuf buf = new ByteBuf(bytes);
        if (buf.readInt() != FILE_VERSION)
            return;
        clientId = buf.read(16);
        int length = buf.readInt();
        for (int i = 0; i < length; i++) {
            int id = buf.readInt();
            Writable<ByteBuf> writable = lookup(id);
            if (writable == null)
                continue;
            writable.read(buf);
        }
    }

    public void write() throws IOException {
        if (!file.exists()) {
            if (!file.createNewFile())
                return;
        }
        ByteBuf buf = new ByteBuf();
        buf.writeInt(FILE_VERSION);
        buf.write(clientId);
        buf.writeInt(writables.size());
        for (Writable<ByteBuf> writable : writables) {
            buf.writeInt(writable.getSerialId());
            writable.write(buf);
        }
        Files.write(file.toPath(), buf.getDataBuffer());
    }

    private Writable<ByteBuf> lookup(int serialId) {
        // Not entirely efficient, but consistent
        for (Writable<ByteBuf> writable : writables) {
            if (writable.getSerialId() == serialId)
                return writable;
        }
        return null;
    }

    public void writable(Writable<ByteBuf> writable) {
        this.writables.add(writable);
    }

    public void writables(Collection<Writable<ByteBuf>> writables) {
        this.writables.addAll(writables);
    }

    public void clear() {
        writables.clear();
    }

    public byte[] getClientId() {
        return clientId;
    }
}
