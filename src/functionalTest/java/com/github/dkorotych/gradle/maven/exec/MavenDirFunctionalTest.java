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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MavenDirFunctionalTest extends AbstractFunctionalTest {
    @TempDir
    private File projectDir;

    static Stream<Arguments> validateMavenVersions() {
        final Stream.Builder<Arguments> builder = Stream.builder();
        for (String gradle : Arrays.asList(minimalSupportedGradleVersion(), maximalSupportedGradleVersion())) {
            for (String maven : supportedMavenVersion()) {
                builder.accept(Arguments.of(gradle, maven));
            }
        }
        return builder.build();
    }

    static Stream<Arguments> validateGradleVersions() {
        final Stream.Builder<Arguments> builder = Stream.builder();
        final String maven = latestMavenVersion();
        for (String gradle : supportedGradleVersion()) {
            builder.accept(Arguments.of(gradle, maven));
        }
        return builder.build();
    }

    @BeforeEach
    void setUp() throws Exception {
        prepareProject(projectDir);
    }

    @ParameterizedTest
    @MethodSource
    void validateMavenVersions(String gradleVersion, String mavenVersion) throws Exception {
        validate(gradleVersion, mavenVersion);
    }

    @ParameterizedTest
    @MethodSource
    void validateGradleVersions(String gradleVersion, String mavenVersion) throws Exception {
        validate(gradleVersion, mavenVersion);
    }

    void validate(String gradleVersion, String mavenVersion) throws Exception {
        final String task = "validate";
        final Path mavenHome = Paths.get(mavenVersion);
        FileUtils.copyDirectory(mavenHome.toFile(), projectDir);
        final BuildResult result = execute(projectDir, gradleVersion, mavenVersion,
                "--build-file", "simple.gradle", task);

        assertThat(result.getOutput())
                .isNotBlank()
                .contains("Validate work with Gradle: " + gradleVersion)
                .contains("Maven: " + mavenHome.getFileName())
                .contains("BUILD SUCCESSFUL")
                .contains("> Task :" + task);
    }
}
