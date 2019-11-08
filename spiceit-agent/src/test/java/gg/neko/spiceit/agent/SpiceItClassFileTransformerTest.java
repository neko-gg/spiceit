package gg.neko.spiceit.agent;

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
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SpiceItClassFileTransformerTest {

    @Test
    void shouldReturnSameBytecodeIfClassNameStartsWithJava() {
        SpiceItClassFileTransformer spiceItClassFileTransformer = new SpiceItClassFileTransformer();
        byte[] testBytecode = "test-bytecode".getBytes(StandardCharsets.UTF_8);
        byte[] transformedBytecode = spiceItClassFileTransformer.transform(null,
                                                                           String.class.getName().replaceAll(Pattern.quote("."), Matcher.quoteReplacement("/")),
                                                                           null,
                                                                           null,
                                                                           testBytecode);

        Assertions.assertEquals(testBytecode, transformedBytecode);
    }

    @Test
    void shouldTransformBytecode() throws IOException, URISyntaxException {
        SpiceItClassFileTransformer spiceItClassFileTransformer = new SpiceItClassFileTransformer();

        JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();
        URL testSource = getClass().getClassLoader().getResource("LogItTestClass.java");
        Assertions.assertNotNull(testSource);
        systemJavaCompiler.run(System.in, System.out, System.err, Paths.get(testSource.toURI()).toString());

        URL testClass = getClass().getClassLoader().getResource("LogItTestClass.class");
        Assertions.assertNotNull(testClass);
        byte[] testBytecode = Files.readAllBytes(Paths.get(testClass.toURI()));
        byte[] transformedBytecode = spiceItClassFileTransformer.transform(null,
                                                                           "LogItTestClass",
                                                                           null,
                                                                           null,
                                                                           Arrays.copyOf(testBytecode, testBytecode.length));

        Assertions.assertNotEquals(testBytecode, transformedBytecode);
    }

}
