/**
 * Copyright 2022 Dmitry Korotych
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.dkorotych.gradle.maven.exec.test;

import org.apache.commons.lang3.StringUtils;
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

@SuppressWarnings("MissingJavadocType")
abstract class AbstractGenerationTask extends MavenDependentTask {
    private final String fileName;
    private final List<String> options;
    @SuppressWarnings("DeclarationOrder")
    protected File outputFile;

    protected AbstractGenerationTask(final String fileName, final List<String> options) {
        this.fileName = fileName;
        this.options = options;
    }

    @Override
    public void setVersion(final String version) {
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
            if (StringUtils.isBlank(description)) {
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
