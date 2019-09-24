package gg.neko.spiceit.maven.plugin;

import gg.neko.spiceit.injector.SpiceItInjector;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Set;

@Mojo(name = "spiceit",
      defaultPhase = LifecyclePhase.PROCESS_CLASSES,
      requiresDependencyCollection = ResolutionScope.TEST,
      requiresDependencyResolution = ResolutionScope.TEST)
public class SpiceItMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Override
    public void execute() {
        String outputDirectory = this.mavenProject.getBuild().getOutputDirectory();
        File[] dependencies = ((Set<Artifact>) this.mavenProject.getArtifacts()).stream()
                                                                                .map(Artifact::getFile)
                                                                                .toArray(File[]::new);

        SpiceItInjector.revise(new File(outputDirectory), dependencies);
    }

}
