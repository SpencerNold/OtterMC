package structures;

public class MutableReference<T> {

	private T t;
	
	public MutableReference(T t) {
		this.t = t;
	}
	
	public MutableReference() {
	}
	
	public T get() {
		return t;
	}
	
	public void set(T t) {
		this.t = t;
	}
}
