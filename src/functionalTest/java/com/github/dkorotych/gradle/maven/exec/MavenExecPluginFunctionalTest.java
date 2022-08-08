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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

class MavenExecPluginFunctionalTest {
    private static File projectDir;

    public static Collection<String> supportedGradleVersion() {
        return Arrays.asList(
//                "5.6.4",
//                "6.0.1",
//                "6.1.1",
//                "6.2.1",
//                "6.3",
//                "6.4.1",
//                "6.5.1",
//                "6.6.1",
//                "6.7.1",
//                "6.8.3",
//                "6.9.2",
//                "7.0.2",
//                "7.1.1",
//                "7.2",
//                "7.3.3",
//                "7.4.2",
                "7.5.1"
        );
    }

    public static Collection<String> supportedMavenVersion() {
        try {
            final URL resource = MavenExecPluginFunctionalTest.class.getResource("/");
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

    public static Stream<Arguments> validate() {
        final Stream.Builder<Arguments> builder = Stream.builder();
        supportedGradleVersion().forEach(gradle -> {
            for (String maven : supportedMavenVersion()) {
                builder.accept(Arguments.of(gradle, maven));
            }
        });
        return builder.build();
    }

    @BeforeAll
    static void beforeAll() throws Exception {
        projectDir = Files.createTempDirectory("functional").toFile();
        final URL resource = MavenExecPluginFunctionalTest.class.getResource("/fixtures/versions");
        final File source = Paths.get(requireNonNull(resource).toURI()).toFile();
        FileUtils.copyDirectory(source, projectDir);
    }

    @Test
    void realUseCaseTest() throws Exception {
        final String task = "realUseCaseTest";
        final String version = supportedGradleVersion().stream().max(Comparator.naturalOrder()).get();
        BuildResult result = execute(version, null, task);

        assertThat(result.getOutput())
                .isNotBlank()
                .contains("Validate work with Gradle: " + version)
                .contains("[INFO] >>> maven-archetype-plugin")
                .contains("[INFO] BUILD SUCCESS")
                .contains("> Task :" + task);
    }

    @ParameterizedTest
    @MethodSource
    void validate(String gradleVersion, String mavenVersion) throws Exception {
        final String task = "validate";
        final BuildResult result = execute(gradleVersion, mavenVersion, task);

        assertThat(result.getOutput())
                .isNotBlank()
                .contains("Validate work with Gradle: " + gradleVersion)
                .contains("Maven: " + Paths.get(mavenVersion).getFileName())
                .contains("BUILD SUCCESSFUL")
                .contains("> Task :" + task);
    }

    private BuildResult execute(String gradleVersion, String mavenVersion, String task) throws Exception {
        if (mavenVersion != null) {
            System.setProperty("maven-home", mavenVersion);
        }

        final boolean localRun = Boolean.parseBoolean(System.getenv("CI"));
        final ArrayList<String> arguments = new ArrayList<>();
        arguments.add(task);
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
}
