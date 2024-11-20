package io.github.ottermc.io;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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

	public static String hash(String s) {
		Charset charset = StandardCharsets.UTF_8;
		return new String(hash(s.getBytes(charset)), charset);
	}

	public static byte[] hash(byte[] bytes) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(bytes);
		} catch (Exception ignored) {
			return null;
		}
	}
}
