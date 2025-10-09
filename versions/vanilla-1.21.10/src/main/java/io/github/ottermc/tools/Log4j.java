package io.github.ottermc.tools;

import io.github.ottermc.logging.UniversalLog4j;
import me.spencernold.transformer.Reflection;
import org.slf4j.Logger;

public class Log4j extends UniversalLog4j {

    private Logger getLogger() {
        return (Logger) Reflection.getStaticValue("net/minecraft/client/MinecraftClient", "LOGGER");
    }

    @Override
    protected void internalLog(String msg) {
        getLogger().info(msg);
    }

    @Override
    protected void internalWarn(String msg) {
        getLogger().warn(msg);
    }

    @Override
    protected void internalError(String msg) {
        getLogger().error(msg);
    }
}
