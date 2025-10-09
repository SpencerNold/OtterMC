package net.ottermc.window;

import net.ottermc.window.versions.Version;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Panel extends JPanel {

    private static final int COMPONENT_SPACING = 20;
    private static final int PREFERRED_COMPONENT_WIDTH = 300;
    private static final int PREFERRED_COMPONENT_HEIGHT = 35;

    private final Window window;

    public Panel(Window window) {
        super(null);
        this.window = window;
        addStaticComponents();
    }

    private void addStaticComponents() {
        List<JComponent> center = getPanelComponents();

        int height = PREFERRED_COMPONENT_HEIGHT + (center.size() - 1);
        int x = (window.getWidth() / 2) - (PREFERRED_COMPONENT_WIDTH / 2);
        int y = (window.getHeight() / 2) - (height / 2);
        for (int i = 0; i < center.size(); i++) {
            JComponent component = center.get(i);
            component.setBounds(x, y + (i * (PREFERRED_COMPONENT_HEIGHT + COMPONENT_SPACING)), PREFERRED_COMPONENT_WIDTH, PREFERRED_COMPONENT_HEIGHT);
            add(component);
        }
    }

    private List<JComponent> getPanelComponents() {
        List<JComponent> components = new ArrayList<>();

        JComboBox<Version> versionDropdown = new JComboBox<>(window.versions.toArray(new Version[0]));
        components.add(versionDropdown);

        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> {
            Version version = (Version) versionDropdown.getSelectedItem();
            if (version == null)
                return;
            window.dispose();
            version.start();
        });
        components.add(playButton);

        return components;
    }
}
