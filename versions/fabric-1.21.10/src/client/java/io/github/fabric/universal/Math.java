package io.github.fabric.universal;

import io.github.ottermc.universal.Mth;
import net.minecraft.util.math.MathHelper;

public class Math extends Mth {

    @Override
    protected float _sin(float value) {
        return MathHelper.sin(value);
    }
}
