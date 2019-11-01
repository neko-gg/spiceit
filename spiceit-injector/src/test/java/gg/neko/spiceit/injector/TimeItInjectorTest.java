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

class TimeItInjectorTest extends AbstractInjectorTest {

    @BeforeAll
    static void mockLogger() throws IOException, URISyntaxException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        setUpMockLoggerAndTestInstance(TIME_IT_TEST_CLASS);
    }

    @BeforeEach
    void resetMock() {
        Mockito.reset(mockLogger);
    }

    @Test
    void shouldLogTimeWhenMethodReturnsCorrectly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method testMethod = testInstance.getClass().getDeclaredMethod(TEST_METHOD, Integer.class);
        testMethod.invoke(testInstance, 42);

        Mockito.verify(mockLogger).info(Mockito.matches(".*TIME.*ms.*" + TEST_METHOD + ".*"));
        Mockito.verifyNoMoreInteractions(mockLogger);
    }

    @Test
    void shouldLogTimeWhenMethodThrowsException() throws NoSuchMethodException {
        Method testMethod = testInstance.getClass().getDeclaredMethod(TEST_EXCEPTION_METHOD, Integer.class);
        Assertions.assertThrows(InvocationTargetException.class, () -> testMethod.invoke(testInstance, 42));

        Mockito.verify(mockLogger).info(Mockito.matches(".*TIME.*ms.*" + TEST_EXCEPTION_METHOD + ".*"));
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
        Constructor<TimeItInjector> declaredTimeItConstructor = TimeItInjector.class.getDeclaredConstructor();
        Assertions.assertFalse(declaredTimeItConstructor.isAccessible());
    }

    @Test
    void shouldThrowExceptionWhenInstantiatedWithReflection() throws ReflectiveOperationException {
        Constructor<TimeItInjector> declaredTimeItConstructor = TimeItInjector.class.getDeclaredConstructor();
        declaredTimeItConstructor.setAccessible(true);
        Assertions.assertThrows(InvocationTargetException.class, declaredTimeItConstructor::newInstance);

        try {
            declaredTimeItConstructor.newInstance();
        } catch (InvocationTargetException e) {
            Assertions.assertNotNull(e.getCause());
            Assertions.assertEquals(UnsupportedOperationException.class, e.getCause().getClass());
        }
    }

}
