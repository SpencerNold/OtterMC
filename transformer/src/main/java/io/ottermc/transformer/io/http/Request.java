package io.ottermc.transformer.io.http;

import java.util.Map;

public class Request {
	
	private final String link;
	private final String method;
	private final Map<String, String> properties;
	private final Map<String, String> parameters;
	private final byte[] body;

	Request(RequestBuilder builder) {
		this.link = builder.link;
		this.method = builder.method.name();
		this.properties = builder.properties;
		this.parameters = builder.parameters;
		this.body = builder.body;
	}
	
	public String getLink() {
		return link;
	}
	
	public String getMethod() {
		return method;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	public byte[] getBody() {
		return body;
	}
}
