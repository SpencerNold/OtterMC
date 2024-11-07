package agent;

import java.lang.instrument.Instrumentation;

public class Agent {

    private static boolean injectionLoad = false;

    public static void premain(String args, Instrumentation instrumentation) {
        launch(args, instrumentation);
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        injectionLoad = true;
        launch(args, instrumentation);
    }

    private static void launch(String args, Instrumentation instrumentation) {
    }

    public static boolean isInjectionLoad() {
        return injectionLoad;
    }
}
