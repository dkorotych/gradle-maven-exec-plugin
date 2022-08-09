package com.github.dkorotych.gradle.maven.exec.test;

import java.util.Collections;

public class GenerateVersionTask extends AbstractGenerationTask {
    public GenerateVersionTask() {
        super("version.txt", Collections.singletonList("--version"));
    }
}
