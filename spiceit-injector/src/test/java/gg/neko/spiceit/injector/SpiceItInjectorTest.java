package gg.neko.spiceit.injector;

import gg.neko.spiceit.annotation.LogIt;
import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
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

    @Test
    void shouldThrowExceptionWhenSpiceItAnnotationIsMissingOrderMethod() throws URISyntaxException, NotFoundException, CannotCompileException {
        CtClass ctClass = ClassPool.getDefault().get(LogIt.class.getName());
        CtMethod ctMethod = ctClass.getDeclaredMethod("order");
        ctClass.removeMethod(ctMethod);
        File classFile = getPath(LOG_IT_TEST_CLASS).toFile();
        Assertions.assertThrows(SpiceItInjectorException.class, () -> this.spiceItInjector.revise(classFile.getParentFile()));
        ctClass.addMethod(ctMethod);
    }

    @Test
    void shouldThrowExceptionWhenAnnotationCannotBeFound() throws IOException, URISyntaxException, NotFoundException {
        Path compiledPath = getPath(LOG_IT_TEST_CLASS);
        byte[] originalBytes = Files.readAllBytes(compiledPath);
        CtClass ctClass = ClassPool.getDefault().makeClass(new ByteArrayInputStream(originalBytes));
        ConstPool constPool = ctClass.getClassFile().getConstPool();

        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation annotation = new Annotation("invalid-annotation", constPool);
        annotationsAttribute.addAnnotation(annotation);

        CtMethod ctMethod = ctClass.getDeclaredMethod(TEST_METHOD);
        ctMethod.getMethodInfo().addAttribute(annotationsAttribute);

        Assertions.assertThrows(SpiceItInjectorException.class, () -> this.spiceItInjector.revise(ctClass.toBytecode()));
    }

}
