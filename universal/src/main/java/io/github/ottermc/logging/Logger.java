package io.github.ottermc.logging;

import java.io.PrintStream;

// Very simple logging, may upgrade in future
public class Logger {

    private static PrintStream out = System.out;
    private static PrintStream err = System.err;

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

    public static void error(String message) {
        print(err, "ERROR", message);
    }

    public static void errorf(String format, Object... args) {
        error(String.format(format, args));
    }

    private static void print(PrintStream stream, String prefix, String message) {
        stream.printf("[OtterMC: %s] %s\n", prefix, message);
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
}
