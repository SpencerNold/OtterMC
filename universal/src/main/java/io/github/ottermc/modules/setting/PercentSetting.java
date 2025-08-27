package io.github.ottermc.modules.setting;

public class PercentSetting extends FloatSetting {

    public PercentSetting(String name, int value, int min, int max) {
        super(name, value / 100d, min / 100d, max / 100d);
    }
}
