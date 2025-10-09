package io.github.ottermc.modules;

import io.github.ottermc.io.ByteBuf;

public abstract class Module implements Writable<ByteBuf> {

    protected final String name;
    protected final Category category;
    private boolean active;

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.active = false;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void update() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) onEnable();
        else onDisable();
    }

    public void toggle() {
        setActive(!active);
    }


    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Setting<?> getSettingByName(String name) {
        Setting<?>[] settings = getSettings();
        for (Setting<?> setting : settings) {
            if (setting.name.equals(name))
                return setting;
        }
        return null;
    }

    public Setting<?>[] getSettings() {
        Writable<ByteBuf>[] writables = getWritables();
        if (writables == null)
            return new Setting<?>[0];
        Setting<?>[] settings = new Setting[writables.length];
        for (int i = 0; i < writables.length; i++) {
            if (writables[i] instanceof Setting<?>)
                settings[i] = (Setting<?>) writables[i];
        }
        int nullCount = 0;
        for (Setting<?> setting : settings) {
            if (setting == null)
                nullCount++;
        }
        if (nullCount != 0) {
            Setting<?>[] copy = new Setting[settings.length - nullCount];
            int index = 0;
            for (Setting<?> setting : settings) {
                if (setting != null) {
                    copy[index] = setting;
                    index++;
                }
            }
            settings = copy;
        }
        return settings;
    }

    public String getDescription() {
        return null;
    }

    public int getSerialId() {
        return (category.getDisplayName() + name).hashCode();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeBoolean(active);
        Writable<ByteBuf>[] writables = getWritables();
        buf.writeInt(writables == null ? 0 : writables.length);
        if (writables != null) {
            for (Writable<ByteBuf> writable : writables) {
                buf.writeInt(writable.getSerialId());
                writable.write(buf);
            }
        }
    }

    @Override
    public void read(ByteBuf buf) {
        setActive(buf.readBoolean());
        int length = buf.readInt();
        for (int i = 0; i < length; i++) {
            int id = buf.readInt();
            Writable<ByteBuf> writable = getWritableById(id);
            if (writable == null)
                continue;
            writable.read(buf);
        }
    }

    private Writable<ByteBuf> getWritableById(int id) {
        Writable<ByteBuf>[] writables = getWritables();
        if (writables == null)
            return null;
        for (Writable<ByteBuf> writable : writables) {
            if (writable.getSerialId() == id)
                return writable;
        }
        return null;
    }

    public boolean shouldRenderInMenu() {
        return true;
    }
}
