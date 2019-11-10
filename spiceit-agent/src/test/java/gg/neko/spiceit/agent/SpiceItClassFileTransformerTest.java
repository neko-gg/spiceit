package gg.neko.spiceit.agent;

import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SpiceItClassFileTransformerTest {

    private final SpiceItClassFileTransformer spiceItClassFileTransformer;

    public SpiceItClassFileTransformerTest() {
        this.spiceItClassFileTransformer = new SpiceItClassFileTransformer();
    }

    @Test
    void shouldReturnNullIfClassNameStartsWithJava() {
        byte[] testBytecode = "test-bytecode".getBytes(StandardCharsets.UTF_8);
        Assertions.assertNotNull(testBytecode);

        byte[] transformedBytecode = this.spiceItClassFileTransformer.transform(null,
                                                                                String.class.getName().replaceAll(Pattern.quote("."), Matcher.quoteReplacement("/")),
                                                                                null,
                                                                                null,
                                                                                testBytecode);
        Assertions.assertNull(transformedBytecode);
    }

    @Test
    void shouldTransformBytecode() throws IOException, URISyntaxException {
        JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();
        URL testSource = getClass().getClassLoader().getResource("LogItTestClass.java");
        Assertions.assertNotNull(testSource);
        systemJavaCompiler.run(System.in, System.out, System.err, Paths.get(testSource.toURI()).toString());

        URL testClass = getClass().getClassLoader().getResource("LogItTestClass.class");
        Assertions.assertNotNull(testClass);
        byte[] testBytecode = Files.readAllBytes(Paths.get(testClass.toURI()));
        byte[] transformedBytecode = this.spiceItClassFileTransformer.transform(null,
                                                                                "LogItTestClass",
                                                                                null,
                                                                                null,
                                                                                testBytecode);

        Assertions.assertNotEquals(testBytecode, transformedBytecode);
    }

    @Test
    void shouldThrowExceptionIfBytecodeTransformationFails() {
        byte[] testBytecode = "invalid-bytecode".getBytes(StandardCharsets.UTF_8);
        Assertions.assertThrows(SpiceItInjectorException.class,
                                () -> this.spiceItClassFileTransformer.transform(null,
                                                                                 "InvalidClass",
                                                                                 null,
                                                                                 null,
                                                                                 testBytecode));
    }

}
