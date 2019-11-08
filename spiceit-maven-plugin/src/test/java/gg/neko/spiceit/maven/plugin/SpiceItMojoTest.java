package gg.neko.spiceit.maven.plugin;

import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

class SpiceItMojoTest {

    @Test
    void mojoShouldTransformClassFiles() throws Exception {
        SpiceItMojo spiceItMojo = new SpiceItMojo();

        Path tempDirectory = Files.createTempDirectory("spiceit-maven-plugin-test-");
        Files.copy(Paths.get(getClass().getClassLoader().getResource("LogItTestClass.java").toURI()),
                   Paths.get(tempDirectory.toString(), "LogItTestClass.java"));

        JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();
        systemJavaCompiler.run(System.in, System.out, System.err, Paths.get(tempDirectory.toString(), "LogItTestClass.java").toString());

        byte[] originalBytes = Files.readAllBytes(Paths.get(tempDirectory.toString(), "LogItTestClass.class"));

        MavenProject mockMavenProject = Mockito.mock(MavenProject.class);
        Build mockBuild = Mockito.mock(Build.class);
        Mockito.when(mockBuild.getOutputDirectory()).thenReturn(tempDirectory.toString());
        Mockito.when(mockMavenProject.getArtifacts()).thenReturn(new HashSet());
        Mockito.when(mockMavenProject.getBuild()).thenReturn(mockBuild);

        Field mavenProjectField = spiceItMojo.getClass().getDeclaredField("mavenProject");
        mavenProjectField.setAccessible(true);
        mavenProjectField.set(spiceItMojo, mockMavenProject);

        Assertions.assertDoesNotThrow(spiceItMojo::execute);

        byte[] transformedBytes = Files.readAllBytes(Paths.get(tempDirectory.toString(), "LogItTestClass.class"));
        Assertions.assertNotEquals(originalBytes, transformedBytes);
    }

}
