package io.github.ottermc.render;

import java.util.function.Supplier;

public class ClientTheme {

    private static final Supplier<Color> defaultSupplier = Color::getDefault;
    private static Supplier<Color> supplier = defaultSupplier;

    public static void setClientTheme(Supplier<Color> supplier) {
        ClientTheme.supplier = supplier == null ? defaultSupplier : supplier;
    }

    public static Color getColor() {
        return supplier.get();
    }
}
