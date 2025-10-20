package io.github.ottermc.render.hud;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Writable;
import io.github.ottermc.universal.Mth;
import io.github.ottermc.universal.UDrawable;

public abstract class MovableComponent extends Component implements Writable<ByteBuf> {

    protected float offsetX = 0, offsetY = 0;
    protected float scale = 1.0f;

    public MovableComponent(int defaultX, int defaultY, int width, int height) {
        super(defaultX, defaultY, width, height);
    }

    public void setOffset(float x, float y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    public float getXOffset() {
        return offsetX;
    }

    public float getYOffset() {
        return offsetY;
    }

    public abstract int getSerialId();

    public void setScale(float scale) {
        this.scale = Mth.clamp(scale, 0.01f, 3.0f);
    }

    public float getScale() {
        return scale;
    }

    public void enableTranslate(Object context) {
        UDrawable.push(context);
        UDrawable.translate(context, getXOffset(), getYOffset());
        UDrawable.scale(context, scale, scale);
        float inv = 1.0f / scale;
        float x = getDefaultX() - (getDefaultX() * inv);
        float y = getDefaultY() - (getDefaultY() * inv);
        UDrawable.translate(context, -x, -y);
    }

    public void disableTranslate(Object context) {
        float inv = 1.0f / scale;
        float x = getDefaultX() - (getDefaultX() * inv);
        float y = getDefaultY() - (getDefaultY() * inv);
        UDrawable.translate(context, -x, -y);
        UDrawable.scale(context, inv, inv);
        UDrawable.translate(context, -getXOffset(), -getYOffset());
        UDrawable.pop(context);
    }

    public abstract void drawDummyObject(Object context);

    public void write(ByteBuf buf) {
        buf.writeFloat(offsetX);
        buf.writeFloat(offsetY);
        buf.writeFloat(scale);
    }

    public void read(ByteBuf buf) {
        offsetX = buf.readFloat();
        offsetY = buf.readFloat();
        scale = buf.readFloat();
    }
}
