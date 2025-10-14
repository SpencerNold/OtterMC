package io.github.ottermc.logging;

import io.github.ottermc.universal.UniversalLog4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;

// Very simple logging, may upgrade in future
public class Logger {

    private static Consumer<String> logConsumer = null;

    public static void log(String message) {
        print("LOG", message);
    }

    public static void log(Object object) {
        log(String.valueOf(object));
    }

    public static void logf(String format, Object... args) {
        log(String.format(format, args));
    }

    public static void warn(String message) {
        print("WARNING", message);
    }

    public static void warnf(String format, Object... args) {
        warn(String.format(format, args));
    }

    public static void error(Throwable throwable) {
        StringWriter string = new StringWriter();
        PrintWriter writer = new PrintWriter(string);
        throwable.printStackTrace(writer);
        error(string.toString());
    }

    public static void error(String message) {
        print("ERROR", message);
    }

    public static void errorf(String format, Object... args) {
        error(String.format(format, args));
    }

    private static void print(String prefix, String message) {
        String value = String.format("[OtterMC: %s] %s", prefix, message);
        if (UniversalLog4j.isActive())
            UniversalLog4j.log(value);
        else
            System.out.println(value);
        if (logConsumer != null)
            logConsumer.accept(value);
    }

    public static void setLogConsumer(Consumer<String> logConsumer) {
        Logger.logConsumer = logConsumer;
    }
}
