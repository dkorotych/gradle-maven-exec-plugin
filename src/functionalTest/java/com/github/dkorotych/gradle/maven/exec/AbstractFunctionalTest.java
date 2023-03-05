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
package com.github.dkorotych.gradle.maven.exec;

import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.util.VersionNumber;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Objects.requireNonNull;

public abstract class AbstractFunctionalTest {
    public static Collection<String> supportedGradleVersion() {
        return Arrays.asList(
                "6.0.1",
                "6.1.1",
                "6.2.1",
                "6.3",
                "6.4.1",
                "6.5.1",
                "6.6.1",
                "6.7.1",
                "6.8.3",
                "6.9.2",
                "7.0.2",
                "7.1.1",
                "7.2",
                "7.3.3",
                "7.4.2",
                "7.5.1",
                "7.6",
                "8.0.2"
        );
    }

    public static Collection<String> supportedMavenVersion() {
        try {
            final URL resource = AbstractFunctionalTest.class.getResource("/");
            final Path dir = Paths.get(Objects.requireNonNull(resource).toURI())
                    .getParent()
                    .getParent()
                    .getParent()
                    .resolve("maven");
            final List<String> versions = new ArrayList<>();
            try (DirectoryStream<Path> paths = Files.newDirectoryStream(dir, entry -> entry.toFile().isDirectory())) {
                for (Path path : paths) {
                    versions.add(path.toAbsolutePath().toString());
                }
            }
            versions.sort(Comparator.naturalOrder());
            return versions;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String maximalSupportedGradleVersion() {
        return toString(supportedGradleVersion().stream()
                .map(VersionNumber::parse)
                .max(Comparator.naturalOrder()));
    }

    public static String minimalSupportedGradleVersion() {
        return toString(supportedGradleVersion().stream()
                .map(VersionNumber::parse)
                .min(Comparator.naturalOrder()));
    }

    public static String latestMavenVersion() {
        final String latestVersion = supportedMavenVersion().stream()
                .map(Paths::get)
                .map(Path::toFile)
                .map(File::getName)
                .map(VersionNumber::parse)
                .max(Comparator.naturalOrder())
                .map(VersionNumber::toString)
                .orElseThrow(RuntimeException::new);
        return supportedMavenVersion().stream()
                .filter(path -> path.endsWith(latestVersion))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    protected static void prepareProject(final File projectDir) throws Exception {
        final URL resource = MavenExecPluginFunctionalTest.class.getResource("/fixtures/versions");
        final File source = Paths.get(requireNonNull(resource).toURI()).toFile();
        FileUtils.copyDirectory(source, projectDir);
    }

    protected BuildResult execute(File projectDir, String gradleVersion, String mavenVersion, String... commandLine) {
        if (mavenVersion != null) {
            System.setProperty("maven-home", mavenVersion);
        }

        final boolean localRun = Boolean.parseBoolean(System.getenv("CI"));
        final ArrayList<String> arguments = new ArrayList<>(Arrays.asList(commandLine));
        if (localRun) {
            arguments.add("--warning-mode");
            arguments.add("all");
        }

        final GradleRunner runner = GradleRunner.create();
        runner.forwardStdOutput(new StringWriter());
        runner.withPluginClasspath();
        runner.withArguments(arguments);
        runner.withProjectDir(projectDir);
        runner.withGradleVersion(gradleVersion);
        runner.withDebug(localRun);
        return runner.build();
    }

    private static String toString(Optional<VersionNumber> optionalVersionNumber) {
        return optionalVersionNumber.map(VersionNumber::toString)
                .map(number -> {
                    if ("7.6.0".equals(number)) {
                        return "7.6";
                    }
                    if ("8.0.0".equals(number)) {
                        return "8.0";
                    }
                    return number;
                })
                .orElseThrow(RuntimeException::new);
    }
}
