package net.ottermc.window.components;

import net.ottermc.window.ColorPalette;
import net.ottermc.window.util.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class Panel extends JPanel {

    private final Image background;

    protected Panel(LayoutManager layout) {
        super(layout);
        setBackground(ColorPalette.DARK0.getColor());
        this.background = Frame.loadImage("/background.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackgroundCard(g);
    }

    private void drawBackgroundCard(Graphics g) {
        int offsX = (int) (getWidth() * 0.182F), offsY = (int) (getHeight() * 0.182F);
        int width = getWidth() - (offsX * 2);
        int height = getHeight() - (offsY * 2);
        int arc = 50;
        float borderWidth = 2.0F;

        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        Shape roundRect = new RoundRectangle2D.Float(borderWidth / 2f, borderWidth / 2f, width - borderWidth, height - borderWidth, arc, arc);

        // mask
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(Color.WHITE);
        g2d.fill(roundRect);

        // draw the image
        g2d.setComposite(AlphaComposite.SrcIn);
        g2d.drawImage(background, 0, 0, width, height, null);

        // haze
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.setColor(ColorPalette.DARK0.haze(128).getColor());
        g2d.fillRect(0, 0, width, height);

        // border
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.setColor(ColorPalette.DARK1.lighter(28).getColor());
        g2d.draw(roundRect);

        g2d.dispose();

        g.drawImage(buffer, offsX, offsY, null);
    }
}
