package io.github.ottermc.c2;

import me.spencernold.kwaf.Resource;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Service;

import java.io.InputStream;

@Service.Controller(path = "/scripts")
public class ScriptController {

    @Route.File(path = "/index.js", contentType = Route.ContentType.JAVASCRIPT, cacheControl = "no-cache")
    public InputStream indexScript() {
        return Resource.Companion.get("scripts/index.js");
    }

    @Route.File(path = "/module.js", contentType = Route.ContentType.JAVASCRIPT, cacheControl = "no-cache")
    public InputStream moduleScript() {
        return Resource.Companion.get("scripts/module.js");
    }

    @Route.File(path = "/loading.js", contentType = Route.ContentType.JAVASCRIPT, cacheControl = "no-cache")
    public InputStream loadingScript() {
        return Resource.Companion.get("scripts/loading.js");
    }
}
