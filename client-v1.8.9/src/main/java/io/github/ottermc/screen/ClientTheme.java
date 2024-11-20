package io.github.ottermc.screen;

import io.github.ottermc.render.Color;

public class ClientTheme {

    private static Color theme;

    public static void setUseTheme(Color color) {
        theme = color;
    }

    public static void setUseDefault() {
        theme = null;
    }

    public static Color getColor(Color nvl) {
        return theme == null ? nvl : theme;
    }

    public static Color getColor() {
        return getColor(Color.DEFAULT);
    }
}
