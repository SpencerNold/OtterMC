package net.ottermc.window;

import net.ottermc.window.components.Panel;
import net.ottermc.window.components.*;
import net.ottermc.window.versions.Version;

import javax.swing.*;
import java.awt.*;

public class LaunchFrame extends Panel {

    private final Window window;

    public LaunchFrame(Window window) {
        super(null);
        this.window = window;
        addNavBar();
        addSideBar();
        addStaticComponents();
    }

    private void addNavBar() {
        NavigationBar navBar = new NavigationBar();
        int width = window.getWidth();
        int height = (int) (window.getHeight() * 0.125f);
        navBar.setBounds(0, 0, width, height);
        add(navBar);

        Color color = ColorPalette.DARK1.lighter(156).getColor();
        SimpleIcon icon = new SimpleIcon("/icon_white.png", 0, color);
        {
            int w = 56, h = 56;
            int x = 15 + 10;
            int y = (height / 2) - (h / 2);
            icon.setBounds(x, y, w, h);
            navBar.add(icon);
        }
        SimpleText text = new SimpleText("Starting as user: " + Main.username, Font.BOLD, 28.0f, color);
        {
            text.setBounds(icon.getX() + icon.getWidth() + 20, 20, width, height);
            navBar.add(text);
        }
        CloseButton closeButton = new CloseButton();
        {
            closeButton.addActionListener(e -> window.dispose());
            int w = 56, h = 56;
            int x = width - w - 15 - 10;
            int y = (height / 2) - (h / 2);
            closeButton.setBounds(x, y, w, h);
            navBar.add(closeButton);
        }
    }

    private void addSideBar() {
        int offsY = (int) (window.getHeight() * 0.125f) + 10;
        int mOffsY = 64 + 35;
        Color color = ColorPalette.DARK1.lighter(156).getColor();
        IconButton ghBtn = new IconButton("/github_icon.png", 20, color);
        ghBtn.addActionListener(e -> {
            // TODO Open the github
        });
        ghBtn.setBounds(15, offsY, 64, 64);
        add(ghBtn);

        IconButton setBtn = new IconButton("/settings_icon.png", 20, color);
        setBtn.addActionListener(e -> {
            // TODO Open client settings
        });
        setBtn.setBounds(15, offsY + mOffsY, 64, 64);
        add(setBtn);

        IconButton shpBtn = new IconButton("/shop_icon.png", 20, color);
        shpBtn.addActionListener(e -> {
            // TODO Open shop
        });
        shpBtn.setBounds(15, offsY + mOffsY * 2, 64, 64);
        add(shpBtn);
        IconButton newBtn = new IconButton("/changelog_icon.png", 20, color);
        newBtn.addActionListener(e -> {
            // TODO Open changelog
        });
        newBtn.setBounds(15, offsY + mOffsY * 3, 64, 64);
        add(newBtn);
    }

    private void addStaticComponents() {
        JComboBox<Version> versionDropdown = new FancyDropdown<>(window.versions.toArray(new Version[0]));
        {
            int width = 218, height = 45;
            int x = (window.getWidth() / 2) - (width / 2);
            int y = window.getHeight() - height - 45;
            versionDropdown.setBounds(x, y, width, height);
        }
        add(versionDropdown);

        JButton playButton = new BigButton("PLAY GAME");
        playButton.addActionListener(e -> {
            Version version = (Version) versionDropdown.getSelectedItem();
            if (version == null)
                return;
            window.dispose();
            version.start();
        });
        {
            int width = 364, height = 92;
            int x = (window.getWidth() / 2) - (width / 2);
            int y = (window.getHeight() / 2) - (height / 2);
            playButton.setBounds(x, y, width, height);
        }
        add(playButton);
    }
}
