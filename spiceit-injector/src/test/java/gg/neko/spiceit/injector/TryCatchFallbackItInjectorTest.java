package gg.neko.spiceit.injector;

import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import gg.neko.spiceit.injector.fallbackit.FallbackItInjectorType;
import javassist.CannotCompileException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

class TryCatchFallbackItInjectorTest extends AbstractInjectorTest {

    private static SpiceItInjector spiceItInjector;

    @BeforeAll
    static void setUpInjector() throws IllegalAccessException, IOException, InstantiationException, CannotCompileException, URISyntaxException {
        spiceItInjector = getDefaultSpiceItInjectorBuilder().fallbackItInjector(FallbackItInjectorType.TRY_CATCH.getFallbackItInjector()).build();
        setUpTestInstance(spiceItInjector, FALLBACK_IT_TEST_CLASS);
    }

    @Test
    void shouldInvokeFallbackMethodWhenCallingMethodIsAnnotatedAndThrowsException() throws NoSuchMethodException {
        Method testMethod = testInstance.getClass().getDeclaredMethod(TEST_METHOD, Integer.class);
        Assertions.assertDoesNotThrow(() -> testMethod.invoke(testInstance, 42));
    }

    @Test
    void shouldNotInvokeFallbackMethodWhenCallingMethodIsNotAnnotatedAndThrowsException() throws NoSuchMethodException {
        Method testMethod = testInstance.getClass().getDeclaredMethod(TEST_NOT_ANNOTATED_METHOD, Integer.class);
        InvocationTargetException invocationTargetException = Assertions.assertThrows(InvocationTargetException.class, () -> testMethod.invoke(testInstance, 42));
        Assertions.assertNotNull(invocationTargetException.getCause());
        Assertions.assertEquals(RuntimeException.class, invocationTargetException.getCause().getClass());
    }

    @Test
    void shouldInvokeFallbackMethodWhenCallingMethodIsAnnotatedWithFallbackOnNullSetToTrueAndReturnsNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method testMethod = testInstance.getClass().getDeclaredMethod(TEST_ALT_METHOD, Integer.class);
        Assertions.assertNotNull(testMethod.invoke(testInstance, 42));
    }

    @Test
    void shouldThrowExceptionWhenFallbackMethodDoesNotExist() throws IOException, URISyntaxException {
        Path compiledPath = getPath(FALLBACK_IT_INVALID_METHOD_TEST_CLASS);
        byte[] originalBytes = Files.readAllBytes(compiledPath);

        Assertions.assertThrows(SpiceItInjectorException.class, () -> spiceItInjector.revise(originalBytes));
    }

    @Test
    void shouldThrowExceptionWhenFallbackReturnTypeIsVoid() throws IOException, URISyntaxException {
        Path compiledPath = getPath(FALLBACK_IT_INVALID_RETURN_TEST_CLASS);
        byte[] originalBytes = Files.readAllBytes(compiledPath);

        Assertions.assertThrows(SpiceItInjectorException.class, () -> spiceItInjector.revise(originalBytes));
    }

}
