package com.github.dkorotych.gradle.maven.exec

import org.gradle.process.ExecResult
import org.gradle.util.ConfigureUtil

/**
 * Extension for project to support methods to directly invoke the Maven task execution.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecPluginProjectConvention {
    /**
     * Execute Maven directly in any task.
     * @param configure Configuration closure for execute action
     * @return Result of execution
     */
    ExecResult mavenexec(@DelegatesTo(MavenExecAction) Closure configure) {
        assert configure != null, 'configure closure should not be null'
        MavenExecAction action = new MavenExecAction()
        ConfigureUtil.configure(configure, action)
        action.execute()
    }
}
