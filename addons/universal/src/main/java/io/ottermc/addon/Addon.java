package io.ottermc.addon;

public class Addon {

    private final String name;
    private final String mainClass;
    private final String[] arguments;
    private final Library[] libraries;

    public Addon(String name, String mainClass, String[] arguments, Library[] libraries) {
        this.name = name;
        this.mainClass = mainClass;
        this.arguments = arguments;
        this.libraries = libraries;
    }

    public String getName() {
        return name;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String[] getArguments() {
        return arguments;
    }

    public Library[] getLibraries() {
        return libraries;
    }

    @Override
    public String toString() {
        return name;
    }
}
