package com.github.dkorotych.gradle.maven.exec.test;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public class PrepareMavenTask extends DefaultTask {
    private String version;
    private File outputDirectory;

    public static String createName(String version) {
        return "prepareMaven_" + version;
    }

    public void setVersion(String version) {
        this.version = version;
        Project project = getProject();
        outputDirectory = project.getBuildDir().toPath().resolve("maven").resolve(version).toFile();
        outputDirectory.mkdirs();
        getOutputs().dir(outputDirectory);
        project.getExtensions().getExtraProperties().set("maven-" + version, outputDirectory);
    }

    @Input
    public String getVersion() {
        return version;
    }

    @TaskAction
    public void prepare() {
        try (PrintStream stream = new PrintStream(new File(outputDirectory, "pom.xml"))) {
            stream.println(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<project xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                            "         xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                            "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                            "    <modelVersion>4.0.0</modelVersion>\n" +
                            "    <groupId>com.github.lazybones</groupId>\n" +
                            "    <artifactId>app</artifactId>\n" +
                            "    <version>0.1-SNAPSHOT</version>\n" +
                            "</project>\n");
        } catch (FileNotFoundException e) {
            throw new GradleException("Can't create test environment", e);
        }
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            getProject().exec(execSpec -> {
                execSpec.executable(getLocalMavenExecutable())
                        .workingDir(outputDirectory);
                execSpec.setStandardOutput(outputStream);
                execSpec.setErrorOutput(errorStream);
                execSpec.setArgs(Arrays.asList("wrapper:wrapper", "-Dmaven=" + version));
            }).assertNormalExitValue();
        } catch (Throwable e) {
            String description = errorStream.toString();
            if (description.trim().isEmpty()) {
                description = outputStream.toString();
            }
            if (description.trim().isEmpty()) {
                description = e.getMessage();
            }
            throw new GradleException(description, e);
        }
    }

    private Object getLocalMavenExecutable() {
        final Project project = getProject();
        final String key = "maven";
        Object maven = Optional.of(project)
                .map(Project::getExtensions)
                .map(ExtensionContainer::getExtraProperties)
                .filter(properties -> properties.has(key))
                .map(properties -> properties.get(key))
                .orElse(null);
        if (maven == null) {
            maven = Optional.ofNullable(System.getenv("MAVEN_HOME"))
                    .filter(((Predicate<String>) s -> s.trim().isEmpty()).negate())
                    .map(Paths::get)
                    .map(path -> path.resolve("bin"))
                    .map(path -> path.resolve("mvn"))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .orElseThrow(() -> new GradleException("Maven installation not found by MAVEN_HOME environment variable"));
            project.getExtensions().getExtraProperties().set(key, maven);
        }
        return maven;
    }
}
