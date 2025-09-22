package io.ottermc.transformer.io.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import io.ottermc.transformer.io.InputStreams;
import io.ottermc.transformer.structures.Pair;

public class HttpClient {

	public <T extends Response> Pair<Integer, Optional<T>> sendRequest(Request request, Class<T> responseType) throws IOException {
		String link = String.format("%s?%s", request.getLink(), getJoinedParameters(request.getParameters()));
		HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
		connection.setRequestMethod(request.getMethod());
		for (Entry<String, String> en : request.getProperties().entrySet())
			connection.setRequestProperty(en.getKey(), en.getValue());
		byte[] body = request.getBody();
		if (body != null) {
			connection.setDoOutput(true);
			OutputStream output = connection.getOutputStream();
			output.write(body);
			output.flush();
			output.close();
		}
		int code = connection.getResponseCode();
		if (code == 204 || connection.getContentLength() == 0)
			return new Pair<>(code, Optional.empty());
		InputStream input = connection.getInputStream();
		body = InputStreams.readAllBytes(input);
		if (responseType != null) {
			try {
				Constructor<T> constructor = responseType.getDeclaredConstructor();
				constructor.setAccessible(true);
				T response = constructor.newInstance();
				response.read(body);
				return new Pair<>(code, Optional.of(response));
			} catch (Exception e) {
			}
		}
		return new Pair<>(code, Optional.empty());
	}
	
	private String getJoinedParameters(Map<String, String> parameters) {
		String[] array = new String[parameters.size()];
		int index = 0;
		for (Entry<String, String> entry : parameters.entrySet()) {
			array[index] = String.format("%s=%s", entry.getKey(), entry.getValue());
			index++;
		}
		return String.join("&", array);
	}
}
