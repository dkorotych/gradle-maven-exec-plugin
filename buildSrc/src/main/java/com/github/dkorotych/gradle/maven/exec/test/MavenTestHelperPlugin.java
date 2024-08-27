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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

@SuppressWarnings("MissingJavadocType")
public class MavenTestHelperPlugin implements Plugin<Project> {
    @SuppressWarnings("ArrayTrailingComma")
    private static final String[] SUPPORTED_MAVEN_VERSIONS = {
            "3.0",
            "3.1.1",
            "3.2.5",
            "3.3.9",
            "3.5.4",
            "3.6.3",
            "3.8.8",
            "3.9.4",
            "3.9.5",
            "3.9.6",
            "3.9.7",
            "3.9.8",
            "3.9.9"
    };

    @Override
    public void apply(final Project project) {
        final TaskContainer tasks = project.getTasks();
        final TaskProvider<Task> prepareTestEnvironments = tasks.register("prepareTestEnvironments");
        for (String version : SUPPORTED_MAVEN_VERSIONS) {
            final TaskProvider<PrepareMavenTask> prepareTask = tasks.register(PrepareMavenTask.createName(version),
                    PrepareMavenTask.class, task -> task.setVersion(version));
            final TaskProvider<GenerateVersionTask> generateVersionTask = tasks.register("generateVersion_" + version,
                    GenerateVersionTask.class, task -> {
                        task.setVersion(version);
                        task.dependsOn(prepareTask);
                    });
            final TaskProvider<GenerateHelpTask> generateHelpTask = tasks.register("generateHelp_" + version,
                    GenerateHelpTask.class, task -> {
                        task.setVersion(version);
                        task.dependsOn(prepareTask);
                    });
            prepareTestEnvironments.configure(task -> task.dependsOn(generateVersionTask, generateHelpTask));
        }
    }
}
