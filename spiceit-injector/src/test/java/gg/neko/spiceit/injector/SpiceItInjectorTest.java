package gg.neko.spiceit.injector;

import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

class SpiceItInjectorTest {

    private static final String LOG_IT_TEST_CLASS = "LogItTestClass.class";
    private static final String TIME_IT_TEST_CLASS = "TimeItTestClass.class";
    private static final String LOG_IT_TIME_IT_TEST_CLASS = "LogItTimeItTestClass.class";

    @BeforeEach
    void compileResources() {
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

    @Test
    void shouldReviseClassFilesInDirectory() throws IOException, URISyntaxException {
        Path compiledPath = getPath(LOG_IT_TEST_CLASS);
        byte[] originalBytes = Files.readAllBytes(compiledPath);
        File compiledFile = compiledPath.toFile();

        SpiceItInjector.revise(compiledFile.getParentFile());

        byte[] spicedBytes = Files.readAllBytes(compiledPath);
        Assertions.assertNotEquals(originalBytes, spicedBytes);
    }

    @Test
    void shouldReviseByteArray() throws IOException, URISyntaxException {
        Path compiledPath = getPath(LOG_IT_TEST_CLASS);
        byte[] originalBytes = Files.readAllBytes(compiledPath);

        byte[] spicedBytes = SpiceItInjector.revise(originalBytes);
        Assertions.assertNotEquals(originalBytes, spicedBytes);
    }

    @Test
    void constructorShouldNotBeAccessible() throws ReflectiveOperationException {
        Constructor<SpiceItInjector> declaredSpiceItConstructor = SpiceItInjector.class.getDeclaredConstructor();
        Assertions.assertFalse(declaredSpiceItConstructor.isAccessible());
    }

    @Test
    void shouldAppendClassPathIfProvided() throws URISyntaxException {
        File classFile = getPath(LOG_IT_TEST_CLASS).toFile();
        SpiceItInjector.revise(classFile.getParentFile(), classFile.getParentFile().getParentFile());
        Assertions.assertDoesNotThrow(() -> SpiceItInjector.revise(classFile.getParentFile(), classFile.getParentFile().getParentFile()));
    }

    @Test
    void shouldThrowExceptionWhenInstantiatedWithReflection() throws ReflectiveOperationException {
        Constructor<SpiceItInjector> declaredSpiceItConstructor = SpiceItInjector.class.getDeclaredConstructor();
        declaredSpiceItConstructor.setAccessible(true);
        Assertions.assertThrows(InvocationTargetException.class, declaredSpiceItConstructor::newInstance);

        try {
            declaredSpiceItConstructor.newInstance();
        } catch (InvocationTargetException e) {
            Assertions.assertNotNull(e.getCause());
            Assertions.assertEquals(UnsupportedOperationException.class, e.getCause().getClass());
        }
    }

    @Test
    void shouldThrowExceptionWhenTargetDirectoryIsNotADirectory() throws URISyntaxException {
        File classFile = getPath(LOG_IT_TEST_CLASS).toFile();
        Assertions.assertThrows(SpiceItInjectorException.class, () -> SpiceItInjector.revise(classFile));
    }

    @Test
    void shouldThrowExceptionWhenClassFileCannotBeOverwritten() throws IOException, URISyntaxException {
        File classFile = getPath(LOG_IT_TEST_CLASS).toFile();
        try (FileInputStream inputStream = new FileInputStream(classFile)) {
            FileLock fileLock = inputStream.getChannel().lock(0L, Long.MAX_VALUE, true);
            try {
                Assertions.assertThrows(SpiceItInjectorException.class, () -> SpiceItInjector.revise(classFile.getParentFile()));
            } finally {
                fileLock.release();
            }
        }
    }

    @Test
    void shouldThrowExceptionWhenTargetDirectoryIsNotValid() {
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.toURI()).thenReturn(URI.create("file:///invalid/uri"));
        Assertions.assertThrows(SpiceItInjectorException.class, () -> SpiceItInjector.revise(mockFile));
    }

    @Test
    void shouldThrowExceptionWhenClassBytecodeIsInvalid() {
        Assertions.assertThrows(SpiceItInjectorException.class, () -> SpiceItInjector.revise(new byte[0]));
    }

    @Test
    void shouldThrowExceptionWhenClassFileIsInvalid() throws URISyntaxException, IOException {
        File classFile = getPath(LOG_IT_TEST_CLASS).toFile();
        FileOutputStream fileOutputStream = new FileOutputStream(classFile);
        fileOutputStream.write("invalid-class-file".getBytes(StandardCharsets.UTF_8));
        Assertions.assertThrows(SpiceItInjectorException.class, () -> SpiceItInjector.revise(classFile.getParentFile()));
    }

    @Test
    void shouldThrowExceptionWhenClassPathIsInvalid() throws URISyntaxException {
        File classFile = getPath(LOG_IT_TEST_CLASS).toFile();
        File invalidFile = new File("invalid-file.jar");
        Assertions.assertThrows(SpiceItInjectorException.class, () -> SpiceItInjector.revise(classFile.getParentFile(), invalidFile));
    }

    private Path getPath(String resourceName) throws URISyntaxException {
        URL compiledUrl = SpiceItInjectorTest.class.getClassLoader().getResource(resourceName);
        Assertions.assertNotNull(compiledUrl);
        URI compiledUri = compiledUrl.toURI();
        return Paths.get(compiledUri);
    }

}
