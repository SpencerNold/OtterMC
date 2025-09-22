package io.github.ottermc.pvp.modules.visual;

import io.ottermc.transformer.io.ByteBuf;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.Writable;
import io.github.ottermc.modules.setting.EnumSetting;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.ClientTheme;
import net.minecraft.util.MathHelper;

public class UIScheme extends Module {

    private final EnumSetting<Theme> themeSetting = new EnumSetting<>("Theme", Theme.class, Theme.DEFAULT);

    public UIScheme() {
        super("UI Scheme", CategoryList.VISUAL);
    }

    @Override
    public void onEnable() {
        ClientTheme.setClientTheme(this::getColor);
    }

    @Override
    public void onDisable() {
        ClientTheme.setClientTheme(null);
    }

    private Color getColor() {
        Color color = themeSetting.getEnumValue().color;
        return color == null ? Sinebow.calculate(1.5f, 1.0f, 1.0f) : color;
    }

    @Override
    public Writable<ByteBuf>[] getWritables() {
        return new Storable<?>[]{themeSetting};
    }

    private enum Theme {
        DEFAULT(Color.getDefault()),
        CYAN(new Color(0x006d77)),
        RED(new Color(0xf76464)),
        GREEN(new Color(0x6b9080)),
        GOLD(new Color(0xedc531)),
        RAINBOW(null);

        private final Color color;

        Theme(Color color) {
            this.color = color;
        }
    }

    private static class Sinebow {

        private static final float TWO_PI = (float) (2.0f * Math.PI);
        private static final float TWO_PI_BY_3 = TWO_PI / 3.0f;

        private static Color calculate(float speed, float saturation, float brightness) {
            long now = System.currentTimeMillis();

            // I gotta figure out how to incorporate speed into this
            float phase = (now % 2000 / 1000f) * TWO_PI;

            float r = sin(phase) * 0.5f + 0.5f;
            float g = sin(phase + TWO_PI_BY_3) * 0.5f + 0.5f;
            float b = sin(phase + 2.0f * TWO_PI_BY_3) * 0.5f + 0.5f;

            float gray = (r + g + b) / 3.0f;
            r = gray + (r - gray) * saturation;
            g = gray + (g - gray) * saturation;
            b = gray + (b - gray) * saturation;

            r *= brightness;
            g *= brightness;
            b *= brightness;

            return new Color(r, g, b, 1.0f);
        }

        private static float sin(float x) {
            return MathHelper.sin(x);
        }
    }
}
