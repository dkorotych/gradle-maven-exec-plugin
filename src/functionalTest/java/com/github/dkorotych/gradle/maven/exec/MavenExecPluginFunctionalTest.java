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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class MavenExecPluginFunctionalTest extends AbstractFunctionalTest {
    private File projectDir;

    @BeforeEach
    void setUp() throws Exception {
        projectDir = Files.createTempDirectory("functional").toFile();
        prepareProject(projectDir);
    }

    @AfterEach
    @SuppressWarnings({"checkstyle:EmptyCatchBlock", "PMD.EmptyCatchBlock"})
    void tearDown() {
        try {
            FileUtils.deleteDirectory(projectDir);
        } catch (IOException ignore) {
        }
    }

    @Test
    void realUseCaseTest() {
        final String task = "realUseCaseTest";
        final String version = maximalSupportedGradleVersion();
        final BuildResult result = execute(projectDir, version, null, task);

        assertThat(result.getOutput())
                .isNotBlank()
                .contains("Validate work with Gradle: " + version)
                .contains("[INFO] >>> maven-archetype-plugin")
                .contains("[INFO] BUILD SUCCESS")
                .contains("> Task :" + task);
    }
}
