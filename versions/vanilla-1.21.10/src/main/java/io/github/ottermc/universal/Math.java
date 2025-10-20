package io.github.ottermc.universal;

import net.minecraft.util.math.MathHelper;

public class Math extends Mth {

    @Override
    protected float _sin(float value) {
        return MathHelper.sin(value);
    }
}
