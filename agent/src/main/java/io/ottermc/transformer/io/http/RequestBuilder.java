package io.ottermc.transformer.io.http;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {

	String link;
	Method method;
	final Map<String, String> properties;
	final Map<String, String> parameters;
	byte[] body;
	
	public RequestBuilder(String link, Method method) {
		this.link = link;
		this.method = method;
		this.properties = new HashMap<String, String>();
		this.parameters = new HashMap<String, String>();
	}
	
	public RequestBuilder setLink(String link) {
		this.link = link;
		return this;
	}
	
	public RequestBuilder setMethod(Method method) {
		this.method = method;
		return this;
	}
	
	public RequestBuilder addRequestProperty(String key, String value) {
		properties.put(key, value);
		return this;
	}
	
	public RequestBuilder addRequestParameter(String key, String value) {
		parameters.put(key, value);
		return this;
	}
	
	public RequestBuilder setBody(byte[] body) {
		this.body = body;
		return this;
	}
	
	public Request build() {
		return new Request(this);
	}
}
