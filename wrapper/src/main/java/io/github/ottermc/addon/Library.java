package io.github.ottermc.addon;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Library {

    private final String name;
    private final String url;
    private final String b64;

    public Library(
            @JsonProperty("name") String name,
            @JsonProperty("url") String url,
            @JsonProperty("b64") String b64
    ) {
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

    @JsonProperty("b64")
    public String getBase64() {
        return b64;
    }
}
