package net.ottermc.window.components;

import net.ottermc.window.util.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class SimpleIcon extends JComponent {

    private final Image image;
    private final int arc;

    public SimpleIcon(String resource, int arc, Color color) {
        this.image = tint(Frame.loadImage(resource), color);
        this.arc = arc;
    }

    public SimpleIcon(String resource) {
        this(resource, 0, Color.white);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth(), height = getHeight();

        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        Shape roundRect = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);

        // mask
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(Color.WHITE);
        g2d.fill(roundRect);

        // draw the image
        g2d.setComposite(AlphaComposite.SrcIn);
        g2d.drawImage(image, 0, 0, width, height, null);

        g2d.dispose();

        g.drawImage(buffer, 0, 0, null);
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
