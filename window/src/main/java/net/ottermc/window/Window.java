package net.ottermc.window;

import net.ottermc.window.util.Frame;
import net.ottermc.window.versions.Version;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Window {

    private final JFrame frame;

    private Window(String title) {
        frame = new JFrame(title);
        frame.setIconImage(Frame.loadImage("/icon.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(getScreenSize());
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setContentPane(new LaunchFrame(this));
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

    private Dimension getScreenSize() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int) (dimension.width * 0.6f), (int) (dimension.height * 0.75f));
    }

    public static void create() {
        //FlatDarkLaf.setup();
        new Window("OtterMC Launcher");
    }
}
