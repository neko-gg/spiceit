package gg.neko.spiceit.injector;

import javassist.CannotCompileException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

class LogItInjectorTest extends AbstractInjectorTest {

    @BeforeAll
    static void mockLogger() throws IOException, URISyntaxException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        setUpMockLoggerAndTestInstance(LOG_IT_TEST_CLASS);
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

    @Test
    void constructorShouldNotBeAccessible() throws ReflectiveOperationException {
        Constructor<LogItInjector> declaredLogItConstructor = LogItInjector.class.getDeclaredConstructor();
        Assertions.assertFalse(declaredLogItConstructor.isAccessible());
    }

    @Test
    void shouldThrowExceptionWhenInstantiatedWithReflection() throws ReflectiveOperationException {
        Constructor<LogItInjector> declaredLogItConstructor = LogItInjector.class.getDeclaredConstructor();
        declaredLogItConstructor.setAccessible(true);
        Assertions.assertThrows(InvocationTargetException.class, declaredLogItConstructor::newInstance);

        try {
            declaredLogItConstructor.newInstance();
        } catch (InvocationTargetException e) {
            Assertions.assertNotNull(e.getCause());
            Assertions.assertEquals(UnsupportedOperationException.class, e.getCause().getClass());
        }
    }

}
