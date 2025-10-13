package net.ottermc.window.components;

import net.ottermc.window.ColorPalette;

import javax.swing.*;
import java.awt.*;

public class NavigationBar extends JComponent {

    @Override
    protected void paintComponent(Graphics g) {
        int offsX = 15, offsY = 15;
        int width = getWidth() - offsX * 2;
        int height = getHeight() - offsY * 2;
        int arc = 20;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(ColorPalette.DARK1.getColor());
        g2d.fillRoundRect(getX() + offsX, getY() + offsY, width, height, arc, arc);

        g2d.dispose();
    }
}
