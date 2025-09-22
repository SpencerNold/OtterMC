package io.github.ottermc.addon;

public class Addon {

    private final String name;
    private final String mainClass;
    private final String targetVersion;
    private final String clientVersion;
    private final String[] arguments;
    private final Library[] libraries;

    public Addon(String name, String mainClass, String targetVersion, String clientVersion, String[] arguments, Library[] libraries) {
        this.name = name;
        this.mainClass = mainClass;
        this.targetVersion = targetVersion;
        this.clientVersion = clientVersion;
        this.arguments = arguments;
        this.libraries = libraries;
    }

    public Addon() {
        this(null, null, null, null, new String[0], new Library[0]);
    }

    public String getName() {
        return name;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public String getClientVersion() {
        return clientVersion;
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
