package gg.neko.spiceit.injector.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

// are we really doing this?
class SpiceItInjectorExceptionTest {

    @Test
    void shouldInstantiateExceptionWithoutArgs() {
        SpiceItInjectorException spiceItInjectorException = Assertions.assertDoesNotThrow((ThrowingSupplier<SpiceItInjectorException>) SpiceItInjectorException::new);
        Assertions.assertNotNull(spiceItInjectorException);
    }

    @Test
    void shouldInstantiateExceptionWithMessage() {
        SpiceItInjectorException spiceItInjectorException = Assertions.assertDoesNotThrow(() -> new SpiceItInjectorException("test-exception"));
        Assertions.assertNotNull(spiceItInjectorException);
        Assertions.assertEquals("test-exception", spiceItInjectorException.getMessage());
    }

    @Test
    void shouldInstantiateExceptionWithCause() {
        RuntimeException runtimeException = new RuntimeException();
        SpiceItInjectorException spiceItInjectorException = Assertions.assertDoesNotThrow(() -> new SpiceItInjectorException(runtimeException));
        Assertions.assertNotNull(spiceItInjectorException);
        Assertions.assertEquals(runtimeException, spiceItInjectorException.getCause());
    }

    @Test
    void shouldInstantiateExceptionWithMessageAndCause() {
        RuntimeException runtimeException = new RuntimeException();
        SpiceItInjectorException spiceItInjectorException = Assertions.assertDoesNotThrow(() -> new SpiceItInjectorException("test-exception", runtimeException));
        Assertions.assertNotNull(spiceItInjectorException);
        Assertions.assertEquals("test-exception", spiceItInjectorException.getMessage());
        Assertions.assertEquals(runtimeException, spiceItInjectorException.getCause());
    }

}
