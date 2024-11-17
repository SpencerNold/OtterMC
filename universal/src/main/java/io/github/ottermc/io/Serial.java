package io.github.ottermc.io;

public final class Serial {

	public static byte[] getBytesFromShort(short s) {
		byte[] bytes = new byte[2];
		bytes[1] = (byte) ((s >> 0) & 0xFF);
		bytes[0] = (byte) ((s >> 8) & 0xFF);
		return bytes;
	}
	
	public static byte[] getBytesFromInt(int i) {
		byte[] bytes = new byte[4];
		bytes[3] = (byte) ((i >> 0) & 0xFF);
		bytes[2] = (byte) ((i >> 8) & 0xFF);
		bytes[1] = (byte) ((i >> 16) & 0xFF);
		bytes[0] = (byte) ((i >> 24) & 0xFF);
		return bytes;
	}
	
	public static byte[] getBytesFromLong(long l) {
		byte[] bytes = new byte[8];
		bytes[7] = (byte) ((l >> 0) & 0xFF);
		bytes[6] = (byte) ((l >> 8) & 0xFF);
		bytes[5] = (byte) ((l >> 16) & 0xFF);
		bytes[4] = (byte) ((l >> 24) & 0xFF);
		bytes[3] = (byte) ((l >> 32) & 0xFF);
		bytes[2] = (byte) ((l >> 40) & 0xFF);
		bytes[1] = (byte) ((l >> 48) & 0xFF);
		bytes[0] = (byte) ((l >> 56) & 0xFF);
		return bytes;
	}
	
	public static byte[] getBytesFromFloat(float f) {
		return getBytesFromInt(Float.floatToIntBits(f));
	}
	
	public static byte[] getBytesFromDouble(double d) {
		return getBytesFromLong(Double.doubleToLongBits(d));
	}
	
	public static short getShortFromBytes(byte[] bytes) {
		return (short) (((bytes[0] & 0xFF) << 8) | ((bytes[1] & 0xFF) << 0));
	}
	
	public static int getIntFromBytes(byte[] bytes) {
		return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | ((bytes[3] & 0xFF) << 0);
	}
	
	public static long getLongFromBytes(byte[] bytes) {
		return ((bytes[0] & 0xFF) << 56) | ((bytes[1] & 0xFF) << 48) | ((bytes[2] & 0xFF) << 40) | ((bytes[3] & 0xFF) << 32) | ((bytes[4] & 0xFF) << 24) | ((bytes[5] & 0xFF) << 16) | ((bytes[6] & 0xFF) << 8) | ((bytes[7] & 0xFF) << 0);
	}
	
	public static float getFloatFromBytes(byte[] bytes) {
		return Float.intBitsToFloat(getIntFromBytes(bytes));
	}
	
	public static double getDoubleFromBytes(byte[] bytes) {
		return Double.longBitsToDouble(getLongFromBytes(bytes));
	}
}
