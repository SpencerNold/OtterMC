package io.ottermc.transformer.io;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Secure {

	public static final ByteArrayToStringTransformer BASE64_TRANSFORMER = (bytes) -> Base64.getEncoder().encodeToString(bytes);

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

	public static String hashToString(byte[] bytes, ByteArrayToStringTransformer transformer) {
		return transformer.transform(bytes);
	}

	public static byte[] hash(byte[] bytes) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(bytes);
		} catch (Exception ignored) {
			return null;
		}
	}

	@FunctionalInterface
	public interface ByteArrayToStringTransformer {
		String transform(byte[] bytes);
	}
}
