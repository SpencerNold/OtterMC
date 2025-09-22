package io.github.ottermc.logging;

public abstract class UniversalLog4j {

    private static UniversalLog4j instance = null;

    public static void register(UniversalLog4j log4j) {
        instance = log4j;
    }

    public static void log(String msg) {
        if (instance == null)
            throw new IllegalStateException("UniversalLog4j must be registered before it can be used");
        instance.internalLog(msg);
    }

    public static void warn(String msg) {
        if (instance == null)
            throw new IllegalStateException("UniversalLog4j must be registered before it can be used");
        instance.internalWarn(msg);
    }

    public static void error(String msg) {
        if (instance == null)
            throw new IllegalStateException("UniversalLog4j must be registered before it can be used");
        instance.internalError(msg);
    }

    public static boolean isActive() {
        return instance != null;
    }

    protected abstract void internalLog(String msg);
    protected abstract void internalWarn(String msg);
    protected abstract void internalError(String msg);
}
