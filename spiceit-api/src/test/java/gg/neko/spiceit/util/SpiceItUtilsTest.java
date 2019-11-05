package gg.neko.spiceit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class SpiceItUtilsTest {

    @Test
    void constructorShouldNotBeAccessible() throws ReflectiveOperationException {
        Constructor<SpiceItUtils> declaredConstructor = SpiceItUtils.class.getDeclaredConstructor();
        Assertions.assertFalse(declaredConstructor.isAccessible());
    }

    @Test
    void shouldThrowExceptionWhenInstantiatedWithReflection() throws ReflectiveOperationException {
        Constructor<SpiceItUtils> declaredConstructor = SpiceItUtils.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Assertions.assertThrows(InvocationTargetException.class, declaredConstructor::newInstance);

        try {
            declaredConstructor.newInstance();
        } catch (InvocationTargetException e) {
            Assertions.assertNotNull(e.getCause());
            Assertions.assertEquals(UnsupportedOperationException.class, e.getCause().getClass());
        }
    }

}
