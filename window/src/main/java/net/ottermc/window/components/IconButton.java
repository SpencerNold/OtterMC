package net.ottermc.window.components;

import net.ottermc.window.ColorPalette;
import net.ottermc.window.util.Animation;
import net.ottermc.window.util.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class IconButton extends JButton {

    private final float minScale = 1.0f, maxScale = 1.1f;
    private final Animation animation = new Animation(minScale, maxScale, 15, this::repaint);

    private final Image image;
    private final int arc;

    public IconButton(String resource, int arc, Color color) {
        this.image = tint(Frame.loadImage(resource), color);
        this.arc = arc;
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.PLAIN, 36.0f));
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

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // scale
        float scale = animation.getValue();
        g2d.translate(width / 2.0, height / 2.0);
        g2d.scale(scale, scale);
        g2d.translate(-width / 2.0, -height / 2.0);

        // draw button box
        g2d.setColor(ColorPalette.DARK1.getColor());
        g2d.fillRoundRect(xOffs, yOffs, width, height, arc, arc);

        // unscale
        float inv = 1.0f / scale;
        g2d.translate(width / 2.0, height / 2.0);
        g2d.scale(inv, inv);
        g2d.translate(-width / 2.0, -height / 2.0);

        // image
        int w = image.getWidth(null), h = image.getHeight(null);
        int offsX = (getWidth() / 2) - (w / 2), offsY = (getHeight() / 2) - (h / 2);
        g2d.drawImage(image, offsX, offsY, w, h, null);

        g2d.dispose();
    }

    private BufferedImage tint(Image image, Color color) {
        if (image == null)
            return null;
        BufferedImage tinted = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = tinted.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.setColor(color);
        g2d.fillRect(0, 0, tinted.getWidth(), tinted.getHeight());
        g2d.dispose();
        return tinted;
    }
}
