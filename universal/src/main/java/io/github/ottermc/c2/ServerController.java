package io.github.ottermc.c2;

import agent.Agent;
import agent.State;
import me.spencernold.kwaf.*;
import me.spencernold.kwaf.services.Service;

import java.io.InputStream;
import java.util.concurrent.Executors;

@Service.Controller
public class ServerController {

    @Route.File(path = "/", contentType = Route.ContentType.HTML, cacheControl = "no-store", immutable = false)
    public InputStream indexFile() {
        return Agent.getState() != State.RUNNING ? Resource.Companion.get("loading.html") : Resource.Companion.get("index.html");
    }

    @Route.File(path = "/module", contentType = Route.ContentType.HTML, cacheControl = "no-store", immutable = false)
    public InputStream moduleFile() {
        return Agent.getState() != State.RUNNING ? Resource.Companion.get("loading.html") : Resource.Companion.get("module.html");
    }

    @Route.File(path = "/stylesheet.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream stylesheet() {
        return Resource.Companion.get("stylesheet.css");
    }

    public static void start() {
        WebServer server = new WebServer.Builder(Protocol.HTTP, 80, new Class[] { ServerController.class, APIController.class, AssetController.class, ScriptController.class }, Executors.newCachedThreadPool(), false).build();
        server.start();
    }
}
