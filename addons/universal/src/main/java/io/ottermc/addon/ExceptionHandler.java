package io.ottermc.addon;

@FunctionalInterface
public interface ExceptionHandler {
    void handle(Throwable throwable);
}
