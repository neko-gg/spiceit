package gg.neko.spiceit.injector;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

class LogItInjectorTest extends AbstractInjectorTest {

    private static Logger mockLogger = Mockito.mock(Logger.class);
    private static Object logItTestInstance;

    @BeforeAll
    static void mockLogger() throws IOException, URISyntaxException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        compileResources();

        Path classPath = getPath(LOG_IT_TEST_CLASS);
        File classFile = classPath.toFile();
        SpiceItInjector.revise(classFile.getParentFile());

        CtClass ctClass = ClassPool.getDefault().makeClass(new FileInputStream(classFile));

        Class<?> logItTestClass = ctClass.toClass();
        logItTestInstance = logItTestClass.newInstance();
        Optional<Field> optionalLoggerField = Arrays.stream(logItTestInstance.getClass().getDeclaredFields())
                                                    .filter(field -> Logger.class.equals(field.getType()))
                                                    .findFirst();
        Assertions.assertTrue(optionalLoggerField.isPresent());

        Field loggerField = optionalLoggerField.get();
        Assertions.assertFalse(loggerField.isAccessible());
        Assertions.assertTrue(Modifier.isFinal(loggerField.getModifiers()));

        loggerField.setAccessible(true);
        Field loggerFieldModifiers = Field.class.getDeclaredField("modifiers");
        Assertions.assertFalse(loggerFieldModifiers.isAccessible());

        loggerFieldModifiers.setAccessible(true);
        loggerFieldModifiers.setInt(loggerField, loggerField.getModifiers() & ~Modifier.FINAL);

        loggerField.set(null, mockLogger);
        Mockito.reset(mockLogger);
    }

    @Test
    void shouldLogEntryAndExitWhenMethodReturnsCorrectly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method testMethod = logItTestInstance.getClass().getDeclaredMethod(TEST_METHOD, Integer.class);
        testMethod.invoke(logItTestInstance, 42);

        Mockito.verify(mockLogger).info(Mockito.matches(".*ENTRY.*" + TEST_METHOD + ".*"));
        Mockito.verify(mockLogger).info(Mockito.matches(".*EXIT.*" + TEST_METHOD + ".*42.*"));
        Mockito.verifyNoMoreInteractions(mockLogger);
    }

    @Test
    void shouldLogEntryAndErrorWhenMethodThrowsException() throws NoSuchMethodException {
        Method testMethod = logItTestInstance.getClass().getDeclaredMethod(TEST_EXCEPTION_METHOD, Integer.class);
        Assertions.assertThrows(InvocationTargetException.class, () -> testMethod.invoke(logItTestInstance, 42));

        Mockito.verify(mockLogger).info(Mockito.matches(".*ENTRY.*" + TEST_EXCEPTION_METHOD + ".*"));
        Mockito.verify(mockLogger).error(Mockito.matches(".*ERROR.*" + TEST_EXCEPTION_METHOD + ".*"));
        Mockito.verifyNoMoreInteractions(mockLogger);
    }

    @Test
    void shouldNotLogWhenMethodIsNotAnnotated() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method testMethod = logItTestInstance.getClass().getDeclaredMethod(TEST_NOT_ANNOTATED_METHOD, Integer.class);
        testMethod.invoke(logItTestInstance, 42);

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
