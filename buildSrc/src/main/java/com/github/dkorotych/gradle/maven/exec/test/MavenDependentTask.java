package com.github.dkorotych.gradle.maven.exec.test;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputDirectory;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public abstract class MavenDependentTask extends DefaultTask {
    protected String version;

    public void setVersion(String version) {
        this.version = version;
        Task task = Objects.requireNonNull(getProject().getTasks().findByName(PrepareMavenTask.createName(version)));
        getDependsOn().add(task);
    }

    @OutputDirectory
    protected Path getMavenHome() {
        return Optional.of(getProject())
                .map(Project::getExtensions)
                .map(ExtensionContainer::getExtraProperties)
                .map(properties -> properties.get("maven-" + version))
                .filter(File.class::isInstance)
                .map(File.class::cast)
                .map(File::toPath)
                .orElse(null);
    }

    @Internal
    protected String getMavenExecutable() {
        return Optional.ofNullable(getMavenHome())
                .map(path -> path.resolve("mvnw"))
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .orElse(null);
    }
}
