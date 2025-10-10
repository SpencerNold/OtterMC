package net.ottermc.window.versions;

import net.ottermc.window.Logger;
import net.ottermc.window.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Version {

    private final String name;

    public Version(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public abstract void start();

    @Override
    public String toString() {
        return name;
    }
}
