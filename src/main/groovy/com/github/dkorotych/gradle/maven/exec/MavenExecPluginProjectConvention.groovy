package com.github.dkorotych.gradle.maven.exec

import org.gradle.process.ExecResult
import org.gradle.process.internal.DefaultExecAction
import org.gradle.util.ConfigureUtil

/**
 * Extension for project to support methods to directly invoke the Maven task execution.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecPluginProjectConvention {
    private final DefaultExecAction action

    /**
     * New instance of the Maven convention.
     *
     * @param action Real instance of gradle action for execution
     * @see MavenExecAction
     */
    MavenExecPluginProjectConvention(DefaultExecAction action) {
        this.action = action
    }

    /**
     * Execute Maven directly in any task.
     *
     * @param configure Configuration closure for execute action
     * @return Result of execution
     */
    ExecResult mavenexec(@DelegatesTo(MavenExecAction) Closure<? extends MavenExecAction> configure) {
        assert configure != null, 'configure closure should not be null'
        MavenExecAction mavenExecAction = new MavenExecAction(action)
        ConfigureUtil.configure(configure, mavenExecAction)
        mavenExecAction.execute()
    }
}
