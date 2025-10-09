package io.github.ottermc;

public class StateRegistry {

    private static State state = State.BOOT;

    public static State getState() {
        return state;
    }

    public static void setState(State state) {
        StateRegistry.state = state;
    }
}
