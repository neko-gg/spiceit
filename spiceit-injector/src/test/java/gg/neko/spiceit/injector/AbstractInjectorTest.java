package gg.neko.spiceit.injector;

import org.junit.jupiter.api.Assertions;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

abstract class AbstractInjectorTest {

    static final String LOG_IT_TEST_CLASS = "LogItTestClass.class";
    static final String TIME_IT_TEST_CLASS = "TimeItTestClass.class";
    static final String LOG_IT_TIME_IT_TEST_CLASS = "LogItTimeItTestClass.class";

    static final String TEST_METHOD = "testMethod";
    static final String TEST_EXCEPTION_METHOD = "testExceptionMethod";
    static final String TEST_NOT_ANNOTATED_METHOD = "testNotAnnotatedMethod";

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

    static Path getPath(String resourceName) throws URISyntaxException {
        URL compiledUrl = AbstractInjectorTest.class.getClassLoader().getResource(resourceName);
        Assertions.assertNotNull(compiledUrl);
        URI compiledUri = compiledUrl.toURI();
        return Paths.get(compiledUri);
    }

}
