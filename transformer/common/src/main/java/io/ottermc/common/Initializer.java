package io.ottermc.common;

public class Initializer extends ReflectObject {

    Initializer(Object instance) {
        super(instance);
    }

    public void start() throws Exception {
        invokeMethod("start");
    }
}
