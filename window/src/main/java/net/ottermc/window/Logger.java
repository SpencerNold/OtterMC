package net.ottermc.window;

public class Logger {

    public static void error(Throwable e) {
        e.printStackTrace(System.err);
        error(e.getMessage(), -1);
    }

    public static void error(String message, int code) {
        System.err.println(message);
        System.exit(code);
    }

    public static void log(String message) {
        System.out.println(message);
    }
}
