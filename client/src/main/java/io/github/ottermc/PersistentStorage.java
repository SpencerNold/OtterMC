package io.github.ottermc;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.logging.Logger;
import io.github.ottermc.modules.Writable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class PersistentStorage {

    private static final int FILE_VERSION = 0;

    private final File file;

    private final ArrayList<Writable<ByteBuf>> writables = new ArrayList<>();

    public PersistentStorage(File directory, String identifier) {
        String name = String.valueOf(identifier.hashCode());
        this.file = new File(directory, name);
    }

    public void write() throws IOException {
        if (!file.exists()) {
            if (!file.createNewFile())
                return;
        }
        ByteBuf buffer = new ByteBuf();
        buffer.writeInt(FILE_VERSION);
        buffer.writeInt(writables.size());
        for (Writable<ByteBuf> writable : writables) {
            buffer.writeInt(writable.getSerialId());
            writable.write(buffer);
        }
        Files.write(file.toPath(), buffer.getDataBuffer());
    }

    public void read() throws IOException {
        if (!file.exists())
            return;
        byte[] bytes = Files.readAllBytes(file.toPath());
        if (bytes.length < 8)
            return;
        ByteBuf buf = new ByteBuf(bytes);
        if (buf.readInt() != FILE_VERSION)
            return;
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            int id = buf.readInt();
            Writable<ByteBuf> writable = lookup(id);
            if (writable == null)
                continue;
            writable.read(buf);
        }
    }

    private Writable<ByteBuf> lookup(int serialId) {
        for (Writable<ByteBuf> writable : writables) {
            if (writable.getSerialId() == serialId)
                return writable;
        }
        return null;
    }

    public void register(Writable<ByteBuf> writable) {
        if (validate(writable))
            this.writables.add(writable);
    }

    private boolean validate(Writable<ByteBuf> writable) {
        for (Writable<ByteBuf> w : writables) {
            if (w.getSerialId() == writable.getSerialId()) {
                Logger.error("two writers have the same serial id: " + w.getClass().getName() + " and " + writable.getClass().getName());
                return false;
            }
        }
        return true;
    }
}
