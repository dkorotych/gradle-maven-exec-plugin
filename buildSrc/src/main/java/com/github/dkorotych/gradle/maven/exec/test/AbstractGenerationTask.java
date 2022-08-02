package com.github.dkorotych.gradle.maven.exec.test;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

abstract class AbstractGenerationTask extends MavenDependentTask {
    private final String fileName;
    private final List<String> options;
    protected File outputFile;

    protected AbstractGenerationTask(String fileName, List<String> options) {
        this.fileName = fileName;
        this.options = options;
    }

    @Override
    public void setVersion(String version) {
        super.setVersion(version);
        outputFile = getProject().getProjectDir().toPath()
                .resolve("src/test/resources/fixtures/descriptor")
                .resolve(version)
                .resolve(fileName)
                .toFile();
        getOutputs().file(outputFile);
    }

    @TaskAction
    public void generate() {
        final Project project = getProject();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        try (OutputStream outputStream = Files.newOutputStream(outputFile.toPath())) {
            project.exec(execSpec -> {
                execSpec.executable(getMavenExecutable())
                        .workingDir(getMavenHome());
                execSpec.setStandardOutput(outputStream);
                execSpec.setErrorOutput(errorStream);
                execSpec.setArgs(options);
            }).assertNormalExitValue();
        } catch (Exception e) {
            String description = buildCauseMessagesWithoutLast(e)
                    .collect(Collectors.joining(System.lineSeparator()));
            if (description.trim().isEmpty()) {
                description = e.getMessage();
            }
            throw new GradleException(description, e);
        }
    }

    private Stream<String> buildCauseMessagesWithoutLast(final Exception e) {
        final Iterator<Throwable> iterator = new Iterator<Throwable>() {
            private Throwable current = e;

            @Override
            public boolean hasNext() {
                return current.getCause() != null;
            }

            @Override
            public Throwable next() {
                current = current.getCause();
                return current;
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator,
                        Spliterator.ORDERED | Spliterator.IMMUTABLE), false)
                .filter(exception -> Objects.nonNull(exception.getCause()))
                .map(Throwable::getMessage)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(((Predicate<String>) String::isEmpty).negate());
    }
}
