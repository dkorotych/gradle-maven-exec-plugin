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
package com.github.dkorotych.gradle.maven;

import org.gradle.internal.os.OperatingSystem;
import org.gradle.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A builder that allows you to build an execution command, depending on the operating system and Maven installation
 * directory.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
public class MavenExecutableProvider {
    private final Path mavenHome;
    private final MemoizedSupplier<String> executableSupplier = MemoizedSupplier.of(this::build);

    /**
     * Created a cached Maven executable command.
     *
     * @param mavenHome Maven installation directory
     */
    public MavenExecutableProvider(final Path mavenHome) {
        this.mavenHome = mavenHome;
    }

    /**
     * Get Maven execution command.
     *
     * @return Maven executable command
     */
    public String getExecutable() {
        return executableSupplier.get();
    }

    private String build() {
        final boolean hasWrapper = Optional.ofNullable(mavenHome)
                .flatMap(path -> {
                    try (@SuppressWarnings({"checkstyle:EmptyCatchBlock", "PMD.EmptyCatchBlock"})
                         Stream<Path> files = Files.list(path)) {
                        return files.map(Path::toFile)
                                .filter(MavenDescriptor::isMavenExecutionWrapperFile)
                                .findFirst();
                    } catch (IOException ignored) {
                    }
                    return Optional.empty();
                })
                .isPresent();
        final boolean windows = OperatingSystem.current().isWindows();
        final List<String> parameters = new ArrayList<>();
        if (windows) {
            parameters.add("cmd");
            parameters.add("/c");
        }
        StringBuilder command = new StringBuilder("mvn" + (hasWrapper ? 'w' : ""));
        if (mavenHome != null) {
            final Path pathToRunner;
            if (hasWrapper) {
                pathToRunner = mavenHome.resolve(command.toString());
            } else {
                pathToRunner = mavenHome.resolve("bin").resolve(command.toString());
            }
            command = new StringBuilder(pathToRunner.toAbsolutePath().toString());
        }
        if (windows) {
            final String extension = ".cmd";
            if (hasWrapper) {
                command.append(extension);
            } else {
                final boolean oldVersion = Paths.get(command + ".bat").toFile().exists();
                if (oldVersion) {
                    command.append(".bat");
                } else {
                    command.append(extension);
                }
            }
        }
        parameters.add(command.toString());
        return CollectionUtils.asCommandLine(parameters);
    }
}
