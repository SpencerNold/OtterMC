package net.ottermc.window;

import net.ottermc.window.versions.Version;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Window {

    private final JFrame frame;
    final List<Version> versions;

    private Window(String title, List<Version> versions) {
        this.versions = versions;
        frame = new JFrame(title);
        frame.setIconImage(loadIcon());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(getScreenSize());
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setContentPane(new Panel(this));
        frame.setVisible(true);
        EventQueue.invokeLater(() -> {
            frame.toFront();
            frame.repaint();
            frame.requestFocus();
            frame.setAlwaysOnTop(true);
            frame.toFront();
            frame.setAlwaysOnTop(false);
        });
    }

    public int getWidth() {
        return getScreenSize().width;
    }

    public int getHeight() {
        return getScreenSize().height;
    }

    public void dispose() {
        frame.dispose();
    }

    private Image loadIcon() {
        Image image = null;
        try {
            InputStream input = getClass().getResourceAsStream("/icon.png");
            if (input != null) {
                image = ImageIO.read(input);
                input.close();
            }
        } catch (IOException e) {
            return null;
        }
        return image;
    }

    private Dimension getScreenSize() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int) (dimension.width * 0.75f), (int) (dimension.height * 0.75f));
    }

    public static Window create(List<Version> versions) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        return new Window("OtterMC Launcher", versions);
    }
}
