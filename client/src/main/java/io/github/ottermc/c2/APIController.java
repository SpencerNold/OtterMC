package io.github.ottermc.c2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.ottermc.AbstractSubClient;
import io.github.ottermc.Game;
import io.github.ottermc.State;
import io.github.ottermc.StateRegistry;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.modules.Setting;
import io.github.ottermc.modules.setting.*;
import io.github.ottermc.render.Color;
import io.github.ottermc.universal.UKeyboard;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.http.HttpRequest;
import me.spencernold.kwaf.services.Service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@Service.Controller(path = "/api")
public class APIController {

    @Route(method = Http.Method.GET, path = "/state")
    public State getState() {
        return StateRegistry.getState();
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

    @Route(method = Http.Method.GET, path = "/modules")
    public JsonArray getModules() {
        JsonArray modules = new JsonArray();
        for (Module mod : getModuleManager().getModules()) {
            JsonObject module = jsonize(mod);
            modules.add(module);
        }
        return modules;
    }

    @Route(method = Http.Method.POST, path = "/toggle", input = true)
    public int postToggle(JsonElement request) {
        if (!request.isJsonObject())
            return 1;
        JsonObject object = request.getAsJsonObject();
        if (!object.has("name") || !object.get("name").isJsonPrimitive())
            return 2;
        String name = object.get("name").getAsString();
        if (!object.has("state") || !object.get("state").isJsonPrimitive())
            return 3;
        boolean state = object.get("state").getAsBoolean();
        ModuleManager manager = getModuleManager();
        Module module = manager.find(mod -> mod.getName().equals(name));
        if (module == null)
            return 4;
        if (object.has("settings")) {
            if (!object.get("settings").isJsonArray())
                return 5;
            for (JsonElement element : object.get("settings").getAsJsonArray()) {
                if (!element.isJsonObject())
                    return 6;
                JsonObject obj = element.getAsJsonObject();
                if (!obj.has("name") || !obj.get("name").isJsonPrimitive())
                    return 7;
                String sName = obj.get("name").getAsString();
                Setting<?> setting = module.getSettingByName(sName);
                if (setting == null)
                    return 8;
                if (!obj.has("value"))
                    return 9;
                JsonElement val = obj.get("value");
                if (!val.isJsonPrimitive())
                    return 10;
                switch (setting.getType()) {
                    case BOOLEAN: {
                        ((BooleanSetting) setting).setValue(val.getAsBoolean());
                    }
                    break;
                    case COLOR: {
                        String value = val.getAsString();
                        if (!value.matches("#[0-9a-fA-F]{1,6}"))
                            return 11;
                        value = value.substring(1);
                        if (value.length() != 6 && value.length() != 8)
                            return 12;
                        ((ColorSetting) setting).setValue(new Color(Integer.parseInt(value, 16)));
                    }
                    break;
                    case ENUM: {
                        ((EnumSetting<? extends Enum<?>>) setting).setValue(val.getAsInt());
                    }
                    break;
                    case INT: {
                        ((IntSetting) setting).setValue(val.getAsInt());
                    }
                    break;
                    case FLOAT: {
                        ((FloatSetting) setting).setValue(val.getAsDouble());
                    }
                    break;
                    case KEYBOARD: {
                        int code = UKeyboard.translateNameToKey(val.getAsString());
                        ((KeyboardSetting) setting).setValue(code);
                    }
                    break;
                    case STRING: {
                        ((StringSetting) setting).setValue(val.getAsString());
                    }
                    break;
                }
            }
        }
        if (module.isActive() != state)
            module.setActive(state);
        try {
            Game.game.save();
        } catch (IOException e) {
            return -1;
        }
        return 0;
    }

    private JsonObject jsonize(Module mod) {
        JsonObject module = new JsonObject();
        module.add("name", new JsonPrimitive(mod.getName()));
        module.add("category", new JsonPrimitive(mod.getCategory().getDisplayName()));
        module.add("enabled", new JsonPrimitive(mod.isActive()));
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
                    setting.add("value", new JsonPrimitive(String.format("#%06X", color.getValue(0))));
                    break;
                case ENUM:
                    EnumSetting<? extends Enum<?>> eSetting = (EnumSetting<? extends Enum<?>>) set;
                    setting.add("value", new JsonPrimitive(eSetting.getValue()));
                    JsonArray array = new JsonArray();
                    for (Enum<?> e : eSetting.getEnumConstants()) {
                        String name = e.name();
                        name = name.charAt(0) + name.substring(1).toLowerCase();
                        JsonObject eValue = new JsonObject();
                        eValue.add("index", new JsonPrimitive(e.ordinal()));
                        eValue.add("name", new JsonPrimitive(name));
                        array.add(eValue);
                    }
                    setting.add("constants", array);
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
            Class<?> clazz = Class.forName("io.github.ottermc.SubClient");
            Method method = clazz.getDeclaredMethod("getInstance");
            AbstractSubClient client = (AbstractSubClient) method.invoke(null);
            return client.getModuleManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
