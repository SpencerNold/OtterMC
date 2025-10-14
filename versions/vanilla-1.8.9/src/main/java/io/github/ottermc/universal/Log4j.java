package io.github.ottermc.universal;

import io.github.ottermc.universal.UniversalLog4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j extends UniversalLog4j {

    private final Logger logger = LogManager.getLogger();

    private Logger getLogger() {
        //return (Logger) Reflection.getStaticValue("net/minecraft/client/Minecraft", "logger");
        return logger; // Maybe this is fine, don't need the Minecraft logger?
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
