package net.ottermc.window.components;

import net.ottermc.window.ColorPalette;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CloseButton extends JButton {

    public CloseButton() {
        super("X");
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.PLAIN, 36.0f));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth(), height = getHeight();
        int arc = 20;
        float borderWidth = 2.0f;

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape roundRect = new RoundRectangle2D.Float(borderWidth / 2f, borderWidth / 2f, width - borderWidth, height - borderWidth, arc, arc);

        g2d.setColor(ColorPalette.DARK1.getColor());
        g2d.fill(roundRect);

        Color outlineColor = getModel().isRollover() ? ColorPalette.RED.getColor() : ColorPalette.DARK1.lighter(28).getColor();
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.setColor(outlineColor);
        g2d.draw(roundRect);

        FontMetrics fm = g2d.getFontMetrics();
        int textX = (width - fm.stringWidth(getText())) / 2;
        int textY = (height + fm.getAscent() - fm.getDescent()) / 2;
        g2d.setColor(outlineColor);
        g2d.drawString(getText(), textX, textY);

        g2d.dispose();
    }
}
