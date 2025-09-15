package io.github.ottermc.keybind;

public class Keybind {

	private final int code;
	private final Runnable runnable;
	private boolean state;
	
	public Keybind(int code, Runnable runnable) {
		this.code = code;
		this.runnable = runnable;
	}
	
	public int getCode() {
		return code;
	}
	
	public Runnable getRunnable() {
		return runnable;
	}
	
	public boolean isDown() {
		return state;
	}
	
	public void setDown(boolean state) {
		this.state = state;
	}
}
