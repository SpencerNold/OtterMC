package io.github.ottermc.addon;

public class Library {

    private final String name;
    private final String url;
    private final String b64;

    public Library(String name, String url, String b64) {
        this.name = name;
        this.url = url;
        this.b64 = b64;
    }

    public Library() {
        this(null, null, null);
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return url;
    }

    public String getBase64() {
        return b64;
    }
}
