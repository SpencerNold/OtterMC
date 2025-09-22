package io.github.ottermc.tools;

import io.github.ottermc.logging.UniversalLog4j;
import me.spencernold.transformer.Reflection;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class Log4j extends UniversalLog4j {

    private Logger getLogger() {
        return (Logger) Reflection.getStaticValue("net/minecraft/client/Minecraft", "logger");
    }

    @Override
    protected void internalLog(String msg) {
        getLogger().log(Level.INFO, msg);
    }

    @Override
    protected void internalWarn(String msg) {
        getLogger().log(Level.WARN, msg);
    }

    @Override
    protected void internalError(String msg) {
        getLogger().log(Level.ERROR, msg);
    }
}
