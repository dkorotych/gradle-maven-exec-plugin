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
package com.github.dkorotych.gradle.maven.exec;

import org.apache.commons.lang3.SystemUtils;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class MavenExecPluginFunctionalTest {
    @TempDir
    File projectDir;

    @ParameterizedTest
    @ValueSource(strings = {
//            "5.6.4", "6.0.1", "6.1.1", "6.2.1", "6.3", "6.4.1", "6.5.1", "6.6.1", "6.7.1", "6.8.3", "6.9.2",
//            "7.0.2", "7.1.1", "7.2", "7.3.3", "7.4.2", "7.5"
            "7.5"
    })
    void canRunTask(String version) throws Exception {
        final File source = Paths.get(getClass().getResource("/fixtures/versions").toURI()).toFile();
        FileUtils.copyDirectory(source, projectDir);
        final String task = "realUseCaseTest";

        GradleRunner runner = GradleRunner.create();
        runner.forwardOutput();
        runner.withPluginClasspath();
        runner.withArguments(task, "--warning-mode", "all");
        runner.withProjectDir(projectDir);
        runner.withGradleVersion(version);
        runner.withDebug(true);
        BuildResult result = runner.build();

        assertThat(result.getOutput())
                .isNotBlank()
                .contains("Validate work with Gradle: " + version)
                .contains("[INFO] >>> maven-archetype-plugin")
                .contains("[INFO] BUILD SUCCESS")
                .contains("> Task :" + task);
    }
}
