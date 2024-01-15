package io.github.ottermc.screen.font;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Charset {

	private final Map<Character, int[]> data = new HashMap<>();
	
	public Charset(String name) {
		InputStream stream = getClass().getResourceAsStream(name);
		InputStreamReader reader = new InputStreamReader(stream);
		Gson gson = new GsonBuilder().create();
		JsonElement element = gson.fromJson(reader, JsonElement.class);
		if (element.isJsonObject()) {
			JsonObject object = element.getAsJsonObject();
			for (Entry<String, JsonElement> entry : object.entrySet()) {
				JsonElement value = entry.getValue();
				int[] ints = new int[4];
				if (value.isJsonArray()) {
					JsonArray array = value.getAsJsonArray();
					for (int i = 0; i < 4; i++)
						ints[i] = array.get(i).getAsInt();
				}
				data.put(entry.getKey().charAt(0), ints);
			}
		}
	}
	
	public int[] getData(char c) {
		return data.get(c);
	}
}
