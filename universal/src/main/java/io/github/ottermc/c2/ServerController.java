package io.github.ottermc.c2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.ottermc.api.Initializer;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.modules.Setting;
import io.github.ottermc.modules.setting.*;
import io.github.ottermc.render.Color;
import me.spencernold.kwaf.*;
import me.spencernold.kwaf.http.HttpRequest;
import me.spencernold.kwaf.services.Service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;

@Service.Controller
public class ServerController {

    @Route.File(path = "/")
    public InputStream indexFile() {
        return Resource.Companion.get("index.html");
    }

    @Route.File(path = "/module")
    public InputStream moduleFile() {
        return Resource.Companion.get("module.html");
    }

    @Route(method = Http.Method.GET, path = "/modules")
    public JsonArray getModules() {
        JsonArray modules = new JsonArray();
        for (Module mod : getModuleManager().getModules()) {
            JsonObject module = new JsonObject();
            module.add("name", new JsonPrimitive(mod.getName()));
            module.add("category", new JsonPrimitive(mod.getCategory().getDisplayName()));
            JsonArray settings = new JsonArray();
            for (Setting<?> set : mod.getSettings()) {
                JsonObject setting = new JsonObject();
                setting.add("name", new JsonPrimitive(set.getName()));
                setting.add("type", new JsonPrimitive(set.getType().name()));
                switch (set.getType()) {
                    case BOOLEAN:
                        setting.add("value", new JsonPrimitive((Boolean) set.getValue()));
                        break;
                    case COLOR:
                        Color color = ((ColorSetting) set).getValue();
                        setting.add("value", new JsonPrimitive(Integer.toUnsignedString(color.getValue(), 16)));
                        setting.add("alpha", new JsonPrimitive(color.hasAlpha()));
                        break;
                    case ENUM:
                        EnumSetting<? extends Enum<?>> eSetting = (EnumSetting<? extends Enum<?>>) set;
                        setting.add("value", new JsonPrimitive(eSetting.getValue()));
                        JsonObject eValues = new JsonObject();
                        for (Enum<?> e : eSetting.getEnumConstants()) {
                            eValues.add("index", new JsonPrimitive(e.ordinal()));
                            eValues.add("name", new JsonPrimitive(e.name()));
                        }
                        setting.add("constants", eValues);
                        break;
                    case INT:
                        IntSetting iSetting = (IntSetting) set;
                        setting.add("value", new JsonPrimitive(iSetting.getValue()));
                        setting.add("min", new JsonPrimitive(iSetting.getMinimum()));
                        setting.add("max", new JsonPrimitive(iSetting.getMaximum()));
                        break;
                    case FLOAT:
                        FloatSetting fSetting = (FloatSetting) set;
                        setting.add("value", new JsonPrimitive(fSetting.getValue()));
                        setting.add("min", new JsonPrimitive(fSetting.getMinimum()));
                        setting.add("max", new JsonPrimitive(fSetting.getMaximum()));
                        break;
                    case KEYBOARD:
                        setting.add("value", new JsonPrimitive(((KeyboardSetting) set).getKeyName()));
                        break;
                    case STRING:
                        setting.add("value", new JsonPrimitive((String) set.getValue()));
                        break;
                }
                settings.add(setting);
            }
            module.add("settings", settings);
            modules.add(module);
        }
        return modules;
    }

    @Route(method = Http.Method.POST, path = "/toggle")
    public boolean postToggle(HttpRequest request) {
        return true;
    }

    private ModuleManager getModuleManager() {
        try {
            Class<?> clazz = Class.forName("io.github.ottermc.Client");
            Method method = clazz.getDeclaredMethod("getInstance");
            Initializer client = (Initializer) method.invoke(null);
            return client.getModuleManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void start() {
        WebServer server = new WebServer.Builder(Protocol.HTTP, 80, new Class[] { ServerController.class }, Executors.newCachedThreadPool()).build();
        server.start();
    }
}
