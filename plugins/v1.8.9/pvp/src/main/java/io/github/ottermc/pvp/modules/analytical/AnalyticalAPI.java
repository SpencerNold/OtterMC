package io.github.ottermc.pvp.modules.analytical;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.ottermc.ClientLogger;
import io.github.ottermc.io.http.*;
import structures.Pair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalyticalAPI {
	
	private static final Gson GSON = new GsonBuilder().create();
	
	private final String link;
	private final HttpClient client;
	private final ExecutorService service;
	
	public AnalyticalAPI(String link) {
		this.link = link;
		this.client = new HttpClient();
		this.service = Executors.newCachedThreadPool();
	}
	
	public CompletableFuture<Boolean> checkConnection() {
		Request request = new RequestBuilder(link, Method.GET).build();
		return CompletableFuture.supplyAsync(() -> {
			try {
				Pair<Integer, Optional<Response>> response = client.sendRequest(request, null);
				return response.getKey() == 204;
			} catch (IOException e) {
				return false;
			}
		}, service);
	}
	
	public CompletableFuture<Integer> sendData(StatHelper helper) {
		Request request = new RequestBuilder(link + "/analytical/player", Method.POST)
				.addRequestProperty("key", hexify(generateClientHashedKey()))
				.setBody(GSON.toJson(helper).getBytes(StandardCharsets.UTF_8))
				.build();
		return CompletableFuture.supplyAsync(() -> {
			try {
				Pair<Integer, Optional<Response>> response = client.sendRequest(request, null);
				int code = response.getKey();
				if (code != 204) {
					// TODO Error numbers (for now I'm not using the return value of this function, but I will eventually)
				}
				return 0;
			} catch (IOException e) {
				ClientLogger.display(e);
				return 1;
			}
		}, service);
	}
	
	// clientId = salt generated
	// uuid = player unique id
	// mode = 1 for analytical
	// key = hash(clientId + uuid + mode)
	private byte[] generateClientHashedKey() {
		return new byte[32];
	}
	
	private String hexify(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes)
			builder.append(String.format("%02X", b));
		return builder.toString();
	}
}
