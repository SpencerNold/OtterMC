package io.github.ottermc.c2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.ottermc.UniversalKeyboard;
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
import java.util.Map;
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
            JsonObject module = jsonize(mod);
            modules.add(module);
        }
        return modules;
    }

    @Route(method = Http.Method.GET, path = "/modstat")
    public JsonObject getModStat(HttpRequest request) {
        Map<String, String> parameters = request.getParameters();
        if (!parameters.containsKey("name"))
            return null;
        String name = parameters.get("name").replace('_', ' ');
        Module module = getModuleManager().find(mod -> mod.getName().equals(name));
        if (module == null)
            return null;
        return jsonize(module);
    }

    @Route(method = Http.Method.POST, path = "/toggle")
    public boolean postToggle(JsonElement request) {
        if (!request.isJsonObject())
            return false;
        JsonObject object = request.getAsJsonObject();
        if (!object.has("name") || !object.get("name").isJsonPrimitive())
            return false;
        String name = object.get("name").getAsString();
        if (!object.has("state") || !object.get("state").isJsonPrimitive())
            return false;
        boolean state = object.get("state").getAsBoolean();
        ModuleManager manager = getModuleManager();
        Module module = manager.find(mod -> mod.getName().equals(name));
        if (module == null)
            return false;
        if (object.has("settings")) {
            if (!object.get("settings").isJsonArray())
                return false;
            for (JsonElement element : object.get("settings").getAsJsonArray()) {
                if (!element.isJsonObject())
                    return false;
                JsonObject obj = element.getAsJsonObject();
                if (!obj.has("name") || !obj.get("name").isJsonPrimitive())
                    return false;
                String sName = obj.get("name").getAsString();
                Setting<?> setting = module.getSettingByName(sName);
                if (setting == null)
                    return false;
                if (!obj.has("value") || !obj.get("value").isJsonPrimitive())
                    return false;
                switch (setting.getType()) {
                    case BOOLEAN: {
                        ((BooleanSetting) setting).setValue(object.get("value").getAsBoolean());
                    }
                    break;
                    case COLOR: {
                        String value = object.get("value").getAsString();
                        if (!value.matches("#[0-9a-f]{1,8}"))
                            return false;
                        value = value.substring(1);
                        if (value.length() != 6 && value.length() != 8)
                            return false;
                        ((ColorSetting) setting).setValue(new Color(Integer.parseInt(value, 16)));
                    }
                    break;
                    case ENUM: {
                        ((EnumSetting<? extends Enum<?>>) setting).setValue(object.get("value").getAsInt());
                    }
                    break;
                    case INT: {
                        ((IntSetting) setting).setValue(object.get("value").getAsInt());
                    }
                    break;
                    case FLOAT: {
                        ((FloatSetting) setting).setValue(object.get("value").getAsDouble());
                    }
                    break;
                    case KEYBOARD: {
                        int code = UniversalKeyboard.translateNameToKey(object.get("value").getAsString());
                        ((KeyboardSetting) setting).setValue(code);
                    }
                    break;
                    case STRING: {
                        ((StringSetting) setting).setValue(object.get("value").getAsString());
                    }
                    break;
                }
            }
        }
        if (module.isActive() != state)
            module.setActive(state);
        return true;
    }

    private JsonObject jsonize(Module mod) {
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
        return module;
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
        WebServer server = new WebServer.Builder(Protocol.HTTP, 80, new Class[]{ServerController.class}, Executors.newCachedThreadPool()).build();
        server.start();
    }
}
