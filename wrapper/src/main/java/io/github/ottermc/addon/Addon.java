package io.github.ottermc.addon;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Addon {

    private final String name;
    private final String mainClass;
    private final boolean agentic;
    private final String targetVersion;
    private final String clientVersion;
    private final String[] arguments;
    private final Library[] libraries;

    public Addon(
            @JsonProperty("name") String name,
            @JsonProperty("mainClass") String mainClass,
            @JsonProperty("agentic") boolean agentic,
            @JsonProperty("targetVersion") String targetVersion,
            @JsonProperty("clientVersion") String clientVersion,
            @JsonProperty("arguments") String[] arguments,
            @JsonProperty("libraries") Library[] libraries
    ) {
        this.name = name;
        this.mainClass = mainClass;
        this.agentic = agentic;
        this.targetVersion = targetVersion;
        this.clientVersion = clientVersion;
        this.arguments = arguments;
        this.libraries = libraries;
    }

    public Addon() {
        this(null, null, true, null, null, new String[0], new Library[0]);
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

    @JsonProperty("agentic")
    public boolean isAgentic() {
        return agentic;
    }

    @Override
    public String toString() {
        return name;
    }
}
