package gg.neko.spiceit.agent;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.instrument.Instrumentation;

class SpiceItAgentTest {

    @Test
    void shouldAddTransformerToInstrumentation() {
        Instrumentation instrumentation = Mockito.mock(Instrumentation.class);
        SpiceItAgent.premain("", instrumentation);

        Mockito.verify(instrumentation).addTransformer(Mockito.argThat(argument -> argument instanceof SpiceItClassFileTransformer));
        Mockito.verifyNoMoreInteractions(instrumentation);
    }

}
