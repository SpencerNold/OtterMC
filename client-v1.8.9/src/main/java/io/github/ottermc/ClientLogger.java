package io.github.ottermc;

import agent.Agent;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.DrawMainMenuScreenListener;
import io.github.ottermc.events.listeners.RenderGameOverlayListener;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.render.DrawableHelper;

import java.util.ArrayList;
import java.util.Comparator;

public class ClientLogger implements DrawMainMenuScreenListener, RenderGameOverlayListener {

    private static ClientLogger instance;

    private final ArrayList<ErrorBlip> currentDisplayedBlips = new ArrayList<>();
    private DrawableHelper drawable = null;

    public ClientLogger() {
        instance = this;
        EventBus.add(this);
    }

    @Override
    public void onDrawMainMenuScreen(DrawMainMenuScreenEvent event) {
        render();
    }

    @Override
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        render();
    }

    private void render() {
        if (drawable == null || currentDisplayedBlips.isEmpty())
            return;
        int width = 150;
        int height = 40;
        int x = (int) (drawable.getWidth() - width - 5);
        int y = 5;
        for (int i = 0; i < currentDisplayedBlips.size(); i++) {
            ErrorBlip error = currentDisplayedBlips.get(i);
            Severity severity = error.severity;
            drawable.outlineRectangle(x, y + (i * (height + 5)), width, height, severity.colorI, severity.colorO);
        }
    }


    public void postInit() {
        drawable = new DrawableHelper();
    }

    public void displayBlip(Severity severity, String message) {
        currentDisplayedBlips.add(new ErrorBlip(severity, message));
        currentDisplayedBlips.sort(Comparator.comparingInt(e -> e.severity.ordinal()));
    }

    public static void display(Severity severity, String message) {
        if (instance == null)
            return;
        instance.displayBlip(severity, message);
    }

    public static void display(Throwable throwable) {
        display(Severity.ERROR, "Internal Error");
    }

    public static void info(String message) {
        display(Severity.INFO, message);
    }

    public static void warn(String message) {
        display(Severity.WARNING, message);
    }

    public static void error(String message) {
        display(Severity.ERROR, message);
    }

    public static void fatal(String message) {
        display(Severity.FATAL, message);
    }

    public enum Severity {

        // These are just the bootstrap colors
        INFO(new Color(0xFF198754)), WARNING(new Color(0xFFFFC107)), ERROR(new Color(0xFFDC3545)), FATAL(new Color((0xFFABD5BD)));

        private final int colorO;
        private final int colorI;

        Severity(Color color) {
            colorO = color.getValue();
            colorI = color.getValue(0x86);
        }
    }

    private static final class ErrorBlip {

        final Severity severity;
        final String message;

        ErrorBlip(Severity severity, String message) {
            this.severity = severity;
            this.message = message;
        }
    }
}
