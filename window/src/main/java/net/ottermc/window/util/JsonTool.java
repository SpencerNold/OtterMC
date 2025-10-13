package net.ottermc.window.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.ottermc.window.Logger;

public class JsonTool {

    public static JsonPrimitive getChildPrimitiveSafe(JsonElement element, String name) {
        JsonElement child = getChildSafe(element, name, false);
        if (child == null) {
            Logger.error("json primitive parent is null", 5);
            return null;
        }
        if (!child.isJsonPrimitive()) {
            Logger.error("json attribute not of primitive type", 5);
            return null;
        }
        return child.getAsJsonPrimitive();
    }

    public static JsonObject getChildObjectSafe(JsonElement element, String name) {
        JsonObject object = getChildObjectNullOnMissing(element, name);
        if (object == null)
            Logger.error("json attribute not of object type", 5);
        return object;
    }

    public static JsonObject getChildObjectNullOnMissing(JsonElement element, String name) {
        JsonElement child = getChildSafe(element, name, true);
        if (child == null)
            return null;
        if (!child.isJsonObject())
            return null;
        return child.getAsJsonObject();
    }

    public static JsonArray getChildArraySafe(JsonElement element, String name) {
        JsonArray array = getChildArrayNullOnMissing(element, name);
        if (array == null)
            Logger.error("json attribute not of array type", 5);
        return array;
    }

    public static JsonArray getChildArrayNullOnMissing(JsonElement element, String name) {
        JsonElement child = getChildSafe(element, name, true);
        if (child == null)
            return null;
        if (!child.isJsonArray())
            return null;
        return child.getAsJsonArray();
    }

    public static JsonElement getChildSafe(JsonElement element, String name, boolean nullOnMissing) {
        if (element == null) {
            Logger.error("json object is null", 5);
            return null;
        }
        if (!element.isJsonObject()) {
            Logger.error("json is malformed", 5);
            return null;
        }
        JsonObject object = element.getAsJsonObject();
        if (!object.has(name)) {
            if (nullOnMissing)
                return null;
            Logger.error("json missing attribute: " + name + "\n---> " + object, 5);
            return null;
        }
        return object.get(name);
    }
}
