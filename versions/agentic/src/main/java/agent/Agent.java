package agent;

import io.github.ottermc.logging.Logger;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String args, Instrumentation instrumentation) {
        try {
            Client.start(args, instrumentation);
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
