package gg.neko.spiceit.injector;

import gg.neko.spiceit.injector.logit.LogItInjectorType;
import gg.neko.spiceit.injector.timeit.TimeItInjectorType;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.slf4j.Logger;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

abstract class AbstractInjectorTest {

    static final String LOG_IT_TEST_CLASS = "valid/LogItTestClass.class";
    static final String TIME_IT_TEST_CLASS = "valid/TimeItTestClass.class";
    static final String LOG_IT_TIME_IT_TEST_CLASS = "valid/LogItTimeItTestClass.class";

    static final String LOG_IT_INVALID_ENTRY_TEST_CLASS = "invalid/LogItInvalidEntryTestClass.class";
    static final String LOG_IT_INVALID_ERROR_TEST_CLASS = "invalid/LogItInvalidErrorTestClass.class";
    static final String LOG_IT_INVALID_EXIT_TEST_CLASS = "invalid/LogItInvalidExitTestClass.class";
    static final String TIME_IT_INVALID_PATTERN_TEST_CLASS = "invalid/TimeItInvalidPatternTestClass.class";

    static final String TEST_METHOD = "testMethod";
    static final String TEST_EXCEPTION_METHOD = "testExceptionMethod";
    static final String TEST_NOT_ANNOTATED_METHOD = "testNotAnnotatedMethod";

    static final Logger mockLogger = Mockito.mock(Logger.class);
    static Object testInstance;

    static SpiceItInjector.Builder getDefaultSpiceItInjectorBuilder() {
        return SpiceItInjector.builder()
                              .logItInjector(LogItInjectorType.SLF4J.getLogItInjector())
                              .timeItInjector(TimeItInjectorType.SYSTEM_MILLIS.getTimeItInjector());
    }

    static void compileResources() {
        JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();

        Stream.of(LOG_IT_TEST_CLASS,
                  TIME_IT_TEST_CLASS,
                  LOG_IT_TIME_IT_TEST_CLASS,
                  LOG_IT_INVALID_ENTRY_TEST_CLASS,
                  LOG_IT_INVALID_ERROR_TEST_CLASS,
                  LOG_IT_INVALID_EXIT_TEST_CLASS,
                  TIME_IT_INVALID_PATTERN_TEST_CLASS)
              .map(pathString -> pathString.replaceFirst("(.*)\\.class", "$1.java"))
              .map(pathString -> Assertions.assertDoesNotThrow(() -> getPath(pathString)))
              .map(Path::toString)
              .forEach(pathString -> {
                  systemJavaCompiler.run(System.in, System.out, System.err, pathString);
                  Path compiledPath = Paths.get(pathString.replaceFirst("(.*)\\.java", "$1.class"));
                  Assertions.assertTrue(Files.exists(compiledPath));
              });
    }

    static void setUpMockLoggerAndTestInstance(SpiceItInjector spiceItInjector, String testClassFileName) throws IOException, URISyntaxException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        compileResources();

        Path classPath = getPath(testClassFileName);
        File classFile = classPath.toFile();
        spiceItInjector.revise(classFile.getParentFile());

        CtClass ctClass = ClassPool.getDefault().makeClass(new FileInputStream(classFile));

        Class<?> testClass = ctClass.toClass();
        testInstance = testClass.newInstance();
        Optional<Field> optionalLoggerField = Arrays.stream(testInstance.getClass().getDeclaredFields())
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
    }

    static Path getPath(String resourceName) throws URISyntaxException {
        URL compiledUrl = AbstractInjectorTest.class.getClassLoader().getResource(resourceName);
        Assertions.assertNotNull(compiledUrl);
        URI compiledUri = compiledUrl.toURI();
        return Paths.get(compiledUri);
    }

}
