package com.github.dkorotych.gradle.maven.exec

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.extraProperties.set(MavenExec.simpleName, MavenExec)
    }
}
