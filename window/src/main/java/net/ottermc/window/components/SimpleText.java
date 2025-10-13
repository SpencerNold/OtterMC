package net.ottermc.window.components;

import net.ottermc.window.util.Frame;

import javax.swing.*;
import java.awt.*;

public class SimpleText extends JComponent {

    private final String text;
    private final Color color;

    public SimpleText(String text, int fontWeight, float fontSize, Color color) {
        this.text = text;
        this.color = color;
        setFont(Frame.loadFont("/Montserrat-Bold.ttf"));
        setFont(getFont().deriveFont(fontWeight, fontSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(color);
        FontMetrics fm = g2d.getFontMetrics();
        setSize(fm.stringWidth(text), getHeight());
        int textY = (getHeight() / 2) - (fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(text, 0, textY);

        g2d.dispose();
    }
}
