package gg.neko.spiceit.injector;

import gg.neko.spiceit.enumeration.LogLevel;
import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class InjectorUtilsTest {

    @Test
    void shouldGetLoggerFieldWhenAlreadyPresent() throws CannotCompileException, NotFoundException {
        CtClass ctClass = ClassPool.getDefault().makeClass("$TEST_CLASS");
        ctClass.addField(CtField.make("org.slf4j.Logger $TEST_LOGGER = org.slf4j.LoggerFactory.getLogger(\"test-class\");", ctClass));
        CtField loggerField = InjectorUtils.getLoggerField(ctClass);

        Assertions.assertNotNull(loggerField);
        Assertions.assertEquals("$TEST_LOGGER", loggerField.getName());
        Assertions.assertEquals("org.slf4j.Logger", loggerField.getType().getName());
    }

    @Test
    void shouldAddNewLoggerFieldWhenNotAlreadyPresent() throws NotFoundException {
        CtClass ctClass = ClassPool.getDefault().makeClass("$TEST_CLASS");
        CtField loggerField = InjectorUtils.getLoggerField(ctClass);

        Assertions.assertNotNull(loggerField);
        Assertions.assertEquals("org.slf4j.Logger", loggerField.getType().getName());
    }

    @Test
    void shouldGetMethodSignature() throws CannotCompileException {
        CtClass ctClass = ClassPool.getDefault().makeClass("$TEST_CLASS");
        CtMethod ctMethod = CtNewMethod.make("void $TEST_METHOD(Integer param1, String param2) { }", ctClass);
        String methodSignature = InjectorUtils.getMethodSignature(ctMethod);

        Assertions.assertNotNull(methodSignature);
        Assertions.assertEquals("$TEST_METHOD(java.lang.Integer, java.lang.String)", methodSignature);
    }

    @Test
    void shouldGenerateTraceLogPattern() {
        String logPattern = InjectorUtils.logPattern(LogLevel.TRACE, "$TEST_LOGGER", "$TEST_PATTERN");
        Assertions.assertNotNull(logPattern);
        Assertions.assertEquals("$TEST_LOGGER.trace($TEST_PATTERN);", logPattern);
    }

    @Test
    void shouldGenerateDebugLogPattern() {
        String logPattern = InjectorUtils.logPattern(LogLevel.DEBUG, "$TEST_LOGGER", "$TEST_PATTERN");
        Assertions.assertNotNull(logPattern);
        Assertions.assertEquals("$TEST_LOGGER.debug($TEST_PATTERN);", logPattern);
    }

    @Test
    void shouldGenerateInfoLogPattern() {
        String logPattern = InjectorUtils.logPattern(LogLevel.INFO, "$TEST_LOGGER", "$TEST_PATTERN");
        Assertions.assertNotNull(logPattern);
        Assertions.assertEquals("$TEST_LOGGER.info($TEST_PATTERN);", logPattern);
    }

    @Test
    void shouldGenerateWarnLogPattern() {
        String logPattern = InjectorUtils.logPattern(LogLevel.WARN, "$TEST_LOGGER", "$TEST_PATTERN");
        Assertions.assertNotNull(logPattern);
        Assertions.assertEquals("$TEST_LOGGER.warn($TEST_PATTERN);", logPattern);
    }

    @Test
    void shouldGenerateErrorLogPattern() {
        String logPattern = InjectorUtils.logPattern(LogLevel.ERROR, "$TEST_LOGGER", "$TEST_PATTERN");
        Assertions.assertNotNull(logPattern);
        Assertions.assertEquals("$TEST_LOGGER.error($TEST_PATTERN);", logPattern);
    }

    @Test
    void shouldGetCatchExceptionTypeName() {
        CtClass ctClass = InjectorUtils.getCatchExceptionTypeName();
        Assertions.assertNotNull(ctClass);
        Assertions.assertEquals(Throwable.class.getName(), ctClass.getName());
    }

    @Test
    void constructorShouldNotBeAccessible() throws ReflectiveOperationException {
        Constructor<InjectorUtils> declaredConstructor = InjectorUtils.class.getDeclaredConstructor();
        Assertions.assertFalse(declaredConstructor.isAccessible());
    }

    @Test
    void shouldThrowExceptionWhenInstantiatedWithReflection() throws ReflectiveOperationException {
        Constructor<InjectorUtils> declaredConstructor = InjectorUtils.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Assertions.assertThrows(InvocationTargetException.class, declaredConstructor::newInstance);

        try {
            declaredConstructor.newInstance();
        } catch (InvocationTargetException e) {
            Assertions.assertNotNull(e.getCause());
            Assertions.assertEquals(UnsupportedOperationException.class, e.getCause().getClass());
        }
    }

    @Test
    void shouldThrowExceptionWhenFieldTypeCannotBeFound() throws NotFoundException {
        CtField mockCtField = Mockito.mock(CtField.class);
        Mockito.when(mockCtField.getType()).thenThrow(new NotFoundException("test-exception"));

        CtClass mockCtClass = Mockito.mock(CtClass.class);
        Mockito.when(mockCtClass.getDeclaredFields()).thenReturn(new CtField[]{mockCtField});

        Assertions.assertThrows(SpiceItInjectorException.class, () -> InjectorUtils.getLoggerField(mockCtClass));
    }

    @Test
    void shouldThrowExceptionWhenLoggerFieldCannotBeAdded() throws CannotCompileException {
        CtClass ctClass = ClassPool.getDefault().makeClass("$TEST_CLASS");
        CtClass spyCtClass = Mockito.spy(ctClass);
        Mockito.doThrow(new CannotCompileException("test-exception")).when(spyCtClass).addField(Mockito.any());

        Assertions.assertThrows(SpiceItInjectorException.class, () -> InjectorUtils.getLoggerField(spyCtClass));
    }

    @Test
    void shouldThrowExceptionWhenMethodParameterTypeCannotBeFound() throws CannotCompileException {
        CtClass ctClass = ClassPool.getDefault().makeClass("$TEST_CLASS");
        CtMethod ctMethod = CtNewMethod.make(Modifier.PUBLIC,
                                             CtClass.longType,
                                             "$TEST_METHOD",
                                             new CtClass[]{ctClass},
                                             new CtClass[]{},
                                             "return 42;",
                                             ctClass);
        ctClass.setName("$ANOTHER_TEST_CLASS");

        Assertions.assertThrows(SpiceItInjectorException.class, () -> InjectorUtils.getMethodSignature(ctMethod));
    }

}
