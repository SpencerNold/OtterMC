package net.ottermc.window.components;

import net.ottermc.window.ColorPalette;
import net.ottermc.window.util.Frame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class FancyDropdown<T> extends JComboBox<T> {

    private static final ColorPalette background = ColorPalette.DARK1;
    private static final Color textColor = ColorPalette.DARK1.lighter(176).getColor();

    private boolean hovered = false;

    public FancyDropdown(T[] array) {
        super(array);
        setFont(Frame.loadFont("/Montserrat-Bold.ttf"));
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 30));
        setFont(getFont().deriveFont(Font.BOLD, 14f));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFocusable(true);
        setUI(new FancyComboBoxUI());

        // hover detection
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
            @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        int arc = 20;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // background

        g2d.setColor(hovered ? background.lighter(24).getColor() : background.getColor());
        g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, arc, arc));

        // border
        g2d.setColor(ColorPalette.DARK1.lighter(12).getColor());
        g2d.draw(new RoundRectangle2D.Float(1.0f, 1.0f, width - 2, height - 2, arc, arc));

        // draw selected item text
        Object selected = getSelectedItem();
        if (selected != null) {
            g2d.setColor(textColor);
            g2d.setFont(getFont());
            FontMetrics fm = g2d.getFontMetrics();
            int textY = (height + fm.getAscent() - fm.getDescent()) / 2;
            g2d.drawString(selected.toString(), 10, textY);
        }

        // draw custom arrow
        int arrowSize = 8;
        int ax = width - 18;
        int ay = height / 2 - 2;
        g2d.setColor(ColorPalette.DARK1.lighter(48).getColor());
        Polygon arrow = new Polygon();
        arrow.addPoint(ax, ay);
        arrow.addPoint(ax + arrowSize, ay);
        arrow.addPoint(ax + arrowSize / 2, ay + arrowSize / 2 + 2);
        g2d.fill(arrow);

        g2d.dispose();
    }

    private static class FancyComboBoxUI extends BasicComboBoxUI {
        @Override
        protected ComboPopup createPopup() {
            return new FancyComboPopup(comboBox);
        }

        @Override
        protected JButton createArrowButton() {
            return null;
        }
    }

    private static class FancyComboPopup extends BasicComboPopup {
        public FancyComboPopup(JComboBox<?> combo) {
            super(combo);
            list.setBackground(FancyDropdown.background.getColor());
            list.setForeground(FancyDropdown.textColor);
        }
    }
}