package io.github.ottermc.addon;

@FunctionalInterface
public interface ExceptionHandler {
    void handle(Throwable throwable);
}
