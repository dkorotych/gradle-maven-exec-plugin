package com.github.dkorotych.gradle.maven.exec.test;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskProvider;

public class MavenTestHelperPlugin implements Plugin<Project> {
    private static final String[] SUPPORTED_MAVEN_VERSIONS = new String[]{
            "3.0.4",
            "3.1.1",
            "3.2.5",
            "3.3.9",
            "3.5.4",
            "3.6.3",
            "3.8.6"
    };

    public void apply(Project project) {
        TaskProvider<Task> prepareTestEnvironments = project.getTasks().register("prepareTestEnvironments");
        for (String version : SUPPORTED_MAVEN_VERSIONS) {
            TaskProvider<PrepareMavenTask> prepareTask = project.getTasks().register(PrepareMavenTask.createName(version),
                    PrepareMavenTask.class, task -> task.setVersion(version));
            TaskProvider<GenerateVersionTask> generateVersionTask = project.getTasks().register("generateVersion_" + version,
                    GenerateVersionTask.class, task -> {
                        task.setVersion(version);
                        task.dependsOn(prepareTask);
                    });
            TaskProvider<GenerateHelpTask> generateHelpTask = project.getTasks().register("generateHelp_" + version,
                    GenerateHelpTask.class, task -> {
                        task.setVersion(version);
                        task.dependsOn(prepareTask);
                    });
            prepareTestEnvironments.configure(task -> task.dependsOn(generateVersionTask, generateHelpTask));
        }
    }
}
