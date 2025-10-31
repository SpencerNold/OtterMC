package io.github.fabric.universal;

import com.mojang.logging.LogUtils;
import io.github.ottermc.universal.UniversalLog4j;
import org.slf4j.Logger;

public class Log4j extends UniversalLog4j {

    private Logger getLogger() {
        return LogUtils.getLogger();
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
