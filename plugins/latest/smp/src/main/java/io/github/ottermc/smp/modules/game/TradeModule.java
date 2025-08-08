package io.github.ottermc.smp.modules.game;

import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;

public abstract class TradeModule extends Module {

    public TradeModule(String name, Category category) {
        super(name, category);
    }

    protected class Trade {
        String target;
        int max;
    }
}
