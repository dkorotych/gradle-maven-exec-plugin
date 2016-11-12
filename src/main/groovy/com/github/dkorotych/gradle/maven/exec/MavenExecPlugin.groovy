package com.github.dkorotych.gradle.maven.exec

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Implementation of the plugin to support the launch Maven tasks.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecPlugin implements Plugin<Project> {

    /**
     * Apply this plugin to the project.
     *
     * @param project Current project
     */
    @Override
    void apply(Project project) {
        project.extensions.extraProperties.set(MavenExec.simpleName, MavenExec)
    }
}
