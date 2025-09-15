package io.github.ottermc.logging;

import java.io.PrintStream;
import java.util.function.Consumer;

// Very simple logging, may upgrade in future
public class Logger {

    public static final me.spencernold.kwaf.logger.Logger KWAF_LOGGER_IMPLEMENTATION = new me.spencernold.kwaf.logger.Logger() {
        @Override
        public void log(Severity severity, String msg) {
            print(severity == Severity.ERROR ? err : out, severity.name(), msg);
        }
    };

    private static PrintStream out = System.out;
    private static PrintStream err = System.err;
    private static Consumer<String> logConsumer = null;

    public static void log(String message) {
        print(out, "LOG", message);
    }

    public static void logf(String format, Object... args) {
        log(String.format(format, args));
    }

    public static void warn(String message) {
        print(out, "WARNING", message);
    }

    public static void warnf(String format, Object... args) {
        warn(String.format(format, args));
    }

    public static void error(Throwable throwable) {
        error(throwable.getClass().getName() + ": " + throwable.getMessage());
    }

    public static void error(String message) {
        print(err, "ERROR", message);
    }

    public static void errorf(String format, Object... args) {
        error(String.format(format, args));
    }

    private static void print(PrintStream stream, String prefix, String message) {
        String value = String.format("[OtterMC: %s] %s\n", prefix, message);
        stream.print(value);
        if (logConsumer != null)
            logConsumer.accept(value);
    }

    public static PrintStream getLoggerOutputStream() {
        return out;
    }

    public static void setLoggerOutputStream(PrintStream out) {
        Logger.out = out;
    }

    public static PrintStream getLoggerErrorStream() {
        return err;
    }

    public static void setLoggerErrorStream(PrintStream err) {
        Logger.err = err;
    }

    public static void setLogConsumer(Consumer<String> logConsumer) {
        Logger.logConsumer = logConsumer;
    }
}
