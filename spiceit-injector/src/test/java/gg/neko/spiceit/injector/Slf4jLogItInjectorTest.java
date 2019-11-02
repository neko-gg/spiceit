package gg.neko.spiceit.injector;

import gg.neko.spiceit.injector.logit.LogItInjectorType;
import javassist.CannotCompileException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

class Slf4jLogItInjectorTest extends AbstractInjectorTest {

    @BeforeAll
    static void mockLogger() throws IOException, URISyntaxException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        SpiceItInjector spiceItInjector = getDefaultSpiceItInjectorBuilder().logItInjector(LogItInjectorType.SLF4J.getLogItInjector()).build();
        setUpMockLoggerAndTestInstance(spiceItInjector, LOG_IT_TEST_CLASS);
    }

    @BeforeEach
    void resetMock() {
        Mockito.reset(mockLogger);
    }

    @Test
    void shouldLogEntryAndExitWhenMethodReturnsCorrectly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method testMethod = testInstance.getClass().getDeclaredMethod(TEST_METHOD, Integer.class);
        testMethod.invoke(testInstance, 42);

        Mockito.verify(mockLogger).info(Mockito.matches(".*ENTRY.*" + TEST_METHOD + ".*"));
        Mockito.verify(mockLogger).info(Mockito.matches(".*EXIT.*" + TEST_METHOD + ".*42.*"));
        Mockito.verifyNoMoreInteractions(mockLogger);
    }

    @Test
    void shouldLogEntryAndErrorWhenMethodThrowsException() throws NoSuchMethodException {
        Method testMethod = testInstance.getClass().getDeclaredMethod(TEST_EXCEPTION_METHOD, Integer.class);
        Assertions.assertThrows(InvocationTargetException.class, () -> testMethod.invoke(testInstance, 42));

        Mockito.verify(mockLogger).info(Mockito.matches(".*ENTRY.*" + TEST_EXCEPTION_METHOD + ".*"));
        Mockito.verify(mockLogger).error(Mockito.matches(".*ERROR.*" + TEST_EXCEPTION_METHOD + ".*"));
        Mockito.verifyNoMoreInteractions(mockLogger);
    }

    @Test
    void shouldNotLogWhenMethodIsNotAnnotated() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method testMethod = testInstance.getClass().getDeclaredMethod(TEST_NOT_ANNOTATED_METHOD, Integer.class);
        testMethod.invoke(testInstance, 42);

        Mockito.verifyNoInteractions(mockLogger);
    }

}
