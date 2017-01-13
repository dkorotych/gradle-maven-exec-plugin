package com.github.dkorotych.gradle.maven.exec

import org.gradle.process.ExecResult
import org.gradle.process.internal.DefaultExecAction

/**
 * Implementation of the action to support the launch Maven.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecAction implements DefaultMavenExecSpec {
    @Delegate(
            excludes = [
                    'setCommandLine',
                    'commandLine',
                    'executable',
                    'setExecutable',
                    'args',
                    'setArgs',
            ],
            interfaces = false)
    private final DefaultExecAction action

    /**
     * New instance of the action.
     *
     * @param action Real instance of gradle action for execution
     */
    MavenExecAction(DefaultExecAction action) {
        this.action = action
    }

    /**
     * Execute action.
     *
     * @return Result of execution
     */
    ExecResult execute() {
        action.setCommandLine(commandLine)
        action.execute()
    }
}
