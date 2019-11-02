package gg.neko.spiceit.injector;

import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

class SpiceItInjectorTest extends AbstractInjectorTest {

    private final SpiceItInjector spiceItInjector;

    SpiceItInjectorTest() {
        this.spiceItInjector = getDefaultSpiceItInjectorBuilder().build();
    }

    @BeforeEach
    void doCompileResources() {
        compileResources();
    }

    @Test
    void shouldReviseClassFilesInDirectory() throws IOException, URISyntaxException {
        Path compiledPath = getPath(LOG_IT_TEST_CLASS);
        byte[] originalBytes = Files.readAllBytes(compiledPath);
        File compiledFile = compiledPath.toFile();

        this.spiceItInjector.revise(compiledFile.getParentFile());

        byte[] spicedBytes = Files.readAllBytes(compiledPath);
        Assertions.assertNotEquals(originalBytes, spicedBytes);
    }

    @Test
    void shouldReviseByteArray() throws IOException, URISyntaxException {
        Path compiledPath = getPath(LOG_IT_TEST_CLASS);
        byte[] originalBytes = Files.readAllBytes(compiledPath);

        byte[] spicedBytes = this.spiceItInjector.revise(originalBytes);
        Assertions.assertNotEquals(originalBytes, spicedBytes);
    }

    @Test
    void shouldAppendClassPathIfProvided() throws URISyntaxException {
        File classFile = getPath(LOG_IT_TEST_CLASS).toFile();
        this.spiceItInjector.revise(classFile.getParentFile(), classFile.getParentFile().getParentFile());
        Assertions.assertDoesNotThrow(() -> this.spiceItInjector.revise(classFile.getParentFile(), classFile.getParentFile().getParentFile()));
    }

    @Test
    void shouldThrowExceptionWhenTargetDirectoryIsNotADirectory() throws URISyntaxException {
        File classFile = getPath(LOG_IT_TEST_CLASS).toFile();
        Assertions.assertThrows(SpiceItInjectorException.class, () -> this.spiceItInjector.revise(classFile));
    }

    @Test
    void shouldThrowExceptionWhenTargetDirectoryIsNotValid() {
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.toURI()).thenReturn(URI.create("file:///invalid/uri"));
        Assertions.assertThrows(SpiceItInjectorException.class, () -> this.spiceItInjector.revise(mockFile));
    }

    @Test
    void shouldThrowExceptionWhenClassBytecodeIsInvalid() {
        Assertions.assertThrows(SpiceItInjectorException.class, () -> this.spiceItInjector.revise(new byte[0]));
    }

    @Test
    void shouldThrowExceptionWhenClassFileIsInvalid() throws URISyntaxException, IOException {
        File classFile = getPath(LOG_IT_TEST_CLASS).toFile();
        FileOutputStream fileOutputStream = new FileOutputStream(classFile);
        fileOutputStream.write("invalid-class-file".getBytes(StandardCharsets.UTF_8));
        Assertions.assertThrows(SpiceItInjectorException.class, () -> this.spiceItInjector.revise(classFile.getParentFile()));
    }

    @Test
    void shouldThrowExceptionWhenClassPathIsInvalid() throws URISyntaxException {
        File classFile = getPath(LOG_IT_TEST_CLASS).toFile();
        File invalidFile = new File("invalid-file.jar");
        Assertions.assertThrows(SpiceItInjectorException.class, () -> this.spiceItInjector.revise(classFile.getParentFile(), invalidFile));
    }

}
