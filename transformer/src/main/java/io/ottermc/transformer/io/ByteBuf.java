package io.ottermc.transformer.io;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteBuf {

	private byte[] data;
	
	public ByteBuf(byte[] bytes) {
		this.data = bytes;
	}
	
	public ByteBuf() {
		this(new byte[0]);
	}
	
	public void write(byte[] bytes) {
		byte[] array = new byte[data.length + bytes.length];
		System.arraycopy(data, 0, array, 0, data.length);
		System.arraycopy(bytes, 0, array, data.length, bytes.length);
		data = array;
	}
	
	public byte[] read(int n) {
		byte[] bytes = Arrays.copyOfRange(data, 0, n);
		data = Arrays.copyOfRange(data, n, data.length);
		return bytes;
	}
	
	public byte readByte() {
		return read(1)[0];
	}
	
	public boolean readBoolean() {
		return readByte() == 1;
	}
	
	public short readShort() {
		return Serial.getShortFromBytes(read(2));
	}
	
	public int readInt() {
		return Serial.getIntFromBytes(read(4));
	}
	
	public long readLong() {
		return Serial.getLongFromBytes(read(8));
	}
	
	public float readFloat() {
		return Serial.getFloatFromBytes(read(4));
	}
	
	public double readDouble() {
		return Serial.getDoubleFromBytes(read(8));
	}
	
	public String readString() {
		return new String(read(readShort()), StandardCharsets.UTF_8);
	}
	
	public void writeByte(byte b) {
		write(new byte[] { b });
	}
	
	public void writeBoolean(boolean b) {
		writeByte((byte) (b ? 1 : 0));
	}
	
	public void writeShort(short s) {
		write(Serial.getBytesFromShort(s));
	}
	
	public void writeInt(int i) {
		write(Serial.getBytesFromInt(i));
	}
	
	public void writeLong(long l) {
		write(Serial.getBytesFromLong(l));
	}
	
	public void writeFloat(float f) {
		write(Serial.getBytesFromFloat(f));
	}
	
	public void writeDouble(double d) {
		write(Serial.getBytesFromDouble(d));
	}
	
	public void writeString(String str) {
		writeShort((short) str.length());
		write(str.getBytes(StandardCharsets.UTF_8));
	}
	
	public byte[] getDataBuffer() {
		return data;
	}
}
