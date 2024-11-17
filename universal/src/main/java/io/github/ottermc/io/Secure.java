package io.github.ottermc.io;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Secure {
	
	public static byte[] random(int n) {
		SecureRandom random;
		try {
			random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			random = new SecureRandom();
		}
		byte[] bytes = new byte[n];
		random.nextBytes(bytes);
		return bytes;
	}
}
