package gg.neko.spiceit.injector;

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

    static final String LOG_IT_TEST_CLASS = "LogItTestClass.class";
    static final String TIME_IT_TEST_CLASS = "TimeItTestClass.class";
    static final String LOG_IT_TIME_IT_TEST_CLASS = "LogItTimeItTestClass.class";

    static final String TEST_METHOD = "testMethod";
    static final String TEST_EXCEPTION_METHOD = "testExceptionMethod";
    static final String TEST_NOT_ANNOTATED_METHOD = "testNotAnnotatedMethod";

    static Logger mockLogger = Mockito.mock(Logger.class);
    static Object testInstance;

    static void compileResources() {
        JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();

        Stream.of(LOG_IT_TEST_CLASS, TIME_IT_TEST_CLASS, LOG_IT_TIME_IT_TEST_CLASS)
              .map(pathString -> pathString.replaceFirst("(.*)\\.class", "$1.java"))
              .map(pathString -> Assertions.assertDoesNotThrow(() -> getPath(pathString)))
              .map(Path::toString)
              .forEach(pathString -> {
                  systemJavaCompiler.run(System.in, System.out, System.err, pathString);
                  Path compiledPath = Paths.get(pathString.replaceFirst("(.*)\\.java", "$1.class"));
                  Assertions.assertTrue(Files.exists(compiledPath));
              });
    }

    static void setUpMockLoggerAndTestInstance(String testClassFileName) throws IOException, URISyntaxException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        compileResources();

        Path classPath = getPath(testClassFileName);
        File classFile = classPath.toFile();
        SpiceItInjector.revise(classFile.getParentFile());

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
