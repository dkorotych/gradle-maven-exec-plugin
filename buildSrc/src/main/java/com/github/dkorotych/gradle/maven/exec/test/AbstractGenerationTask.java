package com.github.dkorotych.gradle.maven.exec.test;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

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
                        .workingDir(System.getProperty("java.io.tmpdir"));
                execSpec.setStandardOutput(outputStream);
                execSpec.setErrorOutput(errorStream);
                execSpec.setArgs(options);
            }).assertNormalExitValue();
        } catch (Throwable e) {
            String description = errorStream.toString();
            if (description.trim().isEmpty()) {
                description = e.getMessage();
            }
            throw new GradleException(description, e);
        }
    }
}
