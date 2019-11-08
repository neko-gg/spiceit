package gg.neko.spiceit.agent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.instrument.Instrumentation;

class SpiceItAgentTest {

    @Test
    void shouldInstantiateClass() {
        SpiceItAgent spiceItAgent = new SpiceItAgent();
        Assertions.assertNotNull(spiceItAgent);
    }

    @Test
    void shouldAddTransformerToInstrumentation() {
        Instrumentation instrumentation = Mockito.mock(Instrumentation.class);
        SpiceItAgent.premain("", instrumentation);

        Mockito.verify(instrumentation).addTransformer(Mockito.argThat(argument -> argument instanceof SpiceItClassFileTransformer));
        Mockito.verifyNoMoreInteractions(instrumentation);
    }

}
