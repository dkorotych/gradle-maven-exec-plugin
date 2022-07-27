/**
 * Copyright 2022 Dmitry Korotych
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
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

public class MavenExecutableProvider {
    private final Path mavenHome;
    private final MemoizedSupplier<String> executableSupplier = MemoizedSupplier.create(this::build);

    /**
     * Create new builder.
     *
     * @param mavenHome Maven installation directory
     */
    public MavenExecutableProvider(Path mavenHome) {
        this.mavenHome = mavenHome;
    }

    public String getExecutable() {
        return executableSupplier.get();
    }

    private String build() {
        final boolean hasWrapper = Optional.ofNullable(mavenHome)
                .flatMap(path -> {
                    try (final Stream<Path> files = Files.list(path)) {
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
        String command = "mvn" + (hasWrapper ? 'w' : "");
        if (mavenHome != null) {
            final Path pathToRunner;
            if (hasWrapper) {
                pathToRunner = mavenHome.resolve(command);
            } else {
                pathToRunner = mavenHome.resolve("bin").resolve(command);
            }
            command = pathToRunner.toAbsolutePath().toString();
        }
        if (windows) {
            String extension = ".cmd";
            if (hasWrapper) {
                command += extension;
            } else {
                boolean oldVersion = Paths.get(command + ".bat").toFile().exists();
                if (oldVersion) {
                    command += ".bat";
                } else {
                    command += extension;
                }
            }
        }
        parameters.add(command);
        return CollectionUtils.asCommandLine(parameters);
    }
}
