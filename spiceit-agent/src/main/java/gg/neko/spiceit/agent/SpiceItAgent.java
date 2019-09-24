package gg.neko.spiceit.agent;

import java.lang.instrument.Instrumentation;

public class SpiceItAgent {

    public static void premain(final String agentArgument, final Instrumentation instrumentation) {
        instrumentation.addTransformer(new SpiceItClassFileTransformer());
    }

}
