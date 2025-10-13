package net.ottermc.window.components;

import net.ottermc.window.ColorPalette;
import net.ottermc.window.util.Animation;
import net.ottermc.window.util.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BigButton extends JButton {

    private final float minScale = 1.0f, maxScale = 1.0257f;
    private final Animation animation = new Animation(minScale, maxScale, 15, this::repaint);

    public BigButton(String text) {
        super(text);
        setFont(Frame.loadFont("/Montserrat-Bold.ttf"));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.BOLD, 28f));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animation.start(maxScale);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animation.start(minScale);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        float offset = (maxScale - minScale) / 2;
        int xOffs = (int) (getWidth() * offset), yOffs = (int) (getHeight() * offset);
        int width = getWidth() - xOffs * 2;
        int height = getHeight() - yOffs * 2;
        int arc = 20;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // scale
        float scale = animation.getValue();
        g2d.translate(width / 2.0, height / 2.0);
        g2d.scale(scale, scale);
        g2d.translate(-width / 2.0, -height / 2.0);

        // draw button box
        g2d.setColor(ColorPalette.ACCENT.getColor());
        g2d.fillRoundRect(xOffs, yOffs, width, height, arc, arc);

        // unscale
        float inv = 1.0f / scale;
        g2d.translate(width / 2.0, height / 2.0);
        g2d.scale(inv, inv);
        g2d.translate(-width / 2.0, -height / 2.0);

        // text
        FontMetrics fm = g2d.getFontMetrics();
        int textX = xOffs + (width - fm.stringWidth(getText())) / 2;
        int textY = yOffs + (height + fm.getAscent() - fm.getDescent()) / 2;
        g2d.setColor(ColorPalette.TEXT.getColor());
        g2d.drawString(getText(), textX, textY);

        g2d.dispose();
    }
}
