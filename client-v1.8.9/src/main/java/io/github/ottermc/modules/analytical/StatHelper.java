package io.github.ottermc.modules.analytical;

public class StatHelper {

	// as long as size (n) is large, it doesn't matter what the individual values are
	// with the sum of the elements, sum of the squares, and the count of elements
	// it should be the case that proper calculations can be done with just this information
	private final int id;
	private float sumX;
	private float sumX2;
	private int size;
	
	public StatHelper(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void add(float value) {
		sumX += value;
		sumX2 += (value * value);
		size++;
	}
	
	public float getSum() {
		return sumX;
	}
	
	public float getSumElementsSquared() {
		return sumX2;
	}
	
	public int getSize() {
		return size;
	}
}
