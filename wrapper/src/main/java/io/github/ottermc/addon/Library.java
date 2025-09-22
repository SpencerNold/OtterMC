package io.github.ottermc.addon;

public class Library {

    private final String name;
    private final String url;

    public Library(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Library() {
        this(null, null);
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return url;
    }
}
