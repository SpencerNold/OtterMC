package io.ottermc.common;

public class Implementation extends ReflectObject {

    public Implementation(Object instance) {
        super(instance);
    }

    public void onEnable() throws Exception {
        invokeMethod("onEnable");
    }
}
