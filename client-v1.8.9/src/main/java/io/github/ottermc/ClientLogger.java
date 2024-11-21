package io.github.ottermc;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.DrawMainMenuScreenListener;
import io.github.ottermc.listeners.RenderGameOverlayListener;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.render.DrawableHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

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
        int x = (int) (drawable.getWidth() - width - 5);
        int y = 5;
        int yOffs = 0;
        for (ErrorBlip error : currentDisplayedBlips) {
            String message = error.message;
            Severity severity = error.severity;

            LinkedList<String> lines = new LinkedList<>();
            lines.addLast(message);
            while (drawable.getStringWidth(lines.getLast()) > width) {
                String line = lines.getLast();
                int lastIndex = line.lastIndexOf(' ');
                while (drawable.getStringWidth(line.substring(0, lastIndex)) > width) {
                    int index = line.lastIndexOf(' ', lastIndex - 1);
                    if (lastIndex == index)
                        break;
                    lastIndex = index;
                }
                lines.removeLast();
                lines.addLast(line.substring(0, lastIndex));
                lines.addLast(line.substring(lastIndex + 1));
            }

            int height = (lines.size() * drawable.getStringHeight()) + 6;

            drawable.outlineRectangle(x, y + yOffs, width, height, severity.colorI, severity.colorO);
            int i = 0;
            for (String line : lines) {
                drawable.drawString(line, x + 3, y + (i * drawable.getStringHeight()) + 3, -1);
                i++;
            }
            yOffs += height;
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
        display(Severity.ERROR, throwable.getMessage());
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
