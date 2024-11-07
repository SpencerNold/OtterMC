package io.github.ottermc.io.http;

public class GenericHttpResponse implements Response {

	private byte[] body;
	
	@Override
	public void read(byte[] body) {
		this.body = body;
	}
	
	public byte[] getBody() {
		return body;
	}
}
