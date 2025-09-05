package io.github.ottermc.c2;

import me.spencernold.kwaf.Resource;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Service;

import java.io.InputStream;

@Service.Controller(path = "/assets")
public class AssetController {

    @Route.File(path = "/background.png", contentType = Route.ContentType.PNG, cacheControl = "public, max-age=31536000, immutable")
    public InputStream background() {
        return Resource.Companion.get("assets/background.png");
    }

    @Route.File(path = "/icon.png", contentType = Route.ContentType.PNG, cacheControl = "public, max-age=31536000, immutable")
    public InputStream icon() {
        return Resource.Companion.get("assets/icon.png");
    }
}
