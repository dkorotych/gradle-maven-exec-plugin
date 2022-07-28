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

import org.apache.commons.lang3.StringUtils;
import org.gradle.api.GradleException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.dkorotych.gradle.maven.MavenDescriptor.isMavenExecutionFile;
import static com.github.dkorotych.gradle.maven.MavenDescriptor.isMavenExecutionWrapperFile;

/**
 * A provider that allows you to find a Maven installation directory.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
public class MavenHomeProvider {
    private Path mavenHome;

    /**
     * Try to find Maven installed in the system.
     * If {@code MAVEN_HOME}, {@code M2_HOME} or {@code maven.home} variables are set in the system and Maven
     * executable files are found in them, then this directory is considered
     *
     * @return true, if Maven founded by environment settings, false - otherwise
     */
    public boolean findMavenHome() {
        return mavenHome != null
                || tryToSetMavenHome("maven.home", true)
                || tryToSetMavenHome("MAVEN_HOME", false)
                || tryToSetMavenHome("M2_HOME", false);
    }

    /**
     * Returns the Maven directory for the process.
     *
     * @return The Maven directory
     */
    public Path getMavenHome() {
        return mavenHome;
    }

    /**
     * Sets the Maven directory for the process.
     * The supplied argument is evaluated as per {@link org.gradle.api.Project#file(Object)}.
     *
     * @param dir The Maven directory
     */
    @SuppressWarnings("PMD.CognitiveComplexity")
    public void setMavenHome(final File dir) {
        mavenHome = Optional.ofNullable(dir)
                .filter(File::exists)
                .map(file -> {
                    try {
                        return file.getCanonicalFile();
                    } catch (IOException e) {
                        throw new GradleException("Can't create canonical file name", e);
                    }
                })
                .map(file -> {
                    File returnValue = null;
                    if (file.isDirectory()) {
                        try (Stream<Path> files = Files.find(file.toPath(), 2, (path, basicFileAttributes) -> {
                            final File currentFile = path.toFile();
                            return isMavenExecutionFile(currentFile) || isMavenExecutionWrapperFile(currentFile);
                        })) {
                            final Path mavenExecutionFile = files.findFirst().orElse(null);
                            if (mavenExecutionFile != null) {
                                setMavenHome(mavenExecutionFile);
                                returnValue = Optional.ofNullable(getMavenHome())
                                        .map(Path::toFile)
                                        .orElse(null);
                            }
                        } catch (IOException e) {
                            throw new GradleException("Can't find Maven execution file", e);
                        }
                    } else {
                        if (isMavenExecutionFile(file)) {
                            final String parentName = Optional.of(file)
                                    .map(File::getParentFile)
                                    .map(File::getName)
                                    .orElse(null);
                            if ("bin".equals(parentName)) {
                                returnValue = Optional.of(file)
                                        .map(File::getParentFile)
                                        .map(File::getParentFile)
                                        .orElse(null);
                            }
                        }
                        if (isMavenExecutionWrapperFile(file)) {
                            returnValue = Optional.of(file)
                                    .map(File::getParentFile)
                                    .orElse(null);
                        }
                    }
                    return returnValue;
                })
                .orElseThrow(() -> new IncorrectMavenInstallationDirectoryException(dir))
                .toPath()
                .normalize()
                .toAbsolutePath();
    }

    /**
     * Sets the Maven directory for the process.
     *
     * @param path The Maven directory
     */
    public void setMavenHome(final String path) {
        final File directory = Optional.ofNullable(path)
                .map(String::trim)
                .filter(((Predicate<String>) String::isEmpty).negate())
                .map(Paths::get)
                .map(Path::toFile)
                .orElse(null);
        setMavenHome(directory);
    }

    /**
     * Sets the Maven directory for the process.
     *
     * @param path The Maven directory
     */
    public void setMavenHome(final Path path) {
        final File directory = Optional.ofNullable(path)
                .map(Path::toFile)
                .orElse(null);
        setMavenHome(directory);
    }

    @SuppressWarnings({"checkstyle:EmptyCatchBlock", "PMD.EmptyCatchBlock"})
    private boolean tryToSetMavenHome(final String name, final boolean property) {
        try {
            final String path = Optional.ofNullable(name)
                    .filter(StringUtils::isNotBlank)
                    .map(s -> property ? System.getProperty(name) : System.getenv(name))
                    .orElse(null);
            if (StringUtils.isNotBlank(path)) {
                setMavenHome(path);
                return true;
            }
        } catch (Exception ignore) {
        }
        return false;
    }
}
