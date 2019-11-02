package gg.neko.spiceit.injector;

import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import gg.neko.spiceit.injector.timeit.TimeItInjectorType;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

class SystemMillisTimeItInjectorTest extends AbstractInjectorTest {

    private static SpiceItInjector spiceItInjector;

    @BeforeAll
    static void mockLogger() throws IOException, URISyntaxException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        spiceItInjector = getDefaultSpiceItInjectorBuilder().timeItInjector(TimeItInjectorType.SYSTEM_MILLIS.getTimeItInjector()).build();
        setUpMockLoggerAndTestInstance(spiceItInjector, TIME_IT_TEST_CLASS);
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
    void shouldThrowExceptionWhenDelegateMethodCannotBeCompiled() throws IOException, URISyntaxException, NotFoundException {
        Path compiledPath = getPath(TIME_IT_INVALID_PATTERN_TEST_CLASS);
        byte[] originalBytes = Files.readAllBytes(compiledPath);
        CtClass ctClass = ClassPool.getDefault().makeClass(new ByteArrayInputStream(originalBytes), false);
        CtMethod ctMethod = ctClass.getDeclaredMethod(TEST_METHOD);
        CtClass invalidCtClass = ClassPool.getDefault().makeClass("invalid-class");
        ctMethod.setExceptionTypes(new CtClass[]{invalidCtClass});
        invalidCtClass.setName("another-invalid-class");
        Assertions.assertThrows(SpiceItInjectorException.class, () -> spiceItInjector.revise(ctClass.toBytecode()));
    }

    @Test
    void shouldThrowExceptionWhenExitStatementCannotBeCompiled() throws IOException, URISyntaxException {
        Path compiledPath = getPath(TIME_IT_INVALID_PATTERN_TEST_CLASS);
        byte[] originalBytes = Files.readAllBytes(compiledPath);

        Assertions.assertThrows(SpiceItInjectorException.class, () -> spiceItInjector.revise(originalBytes));
    }

}
