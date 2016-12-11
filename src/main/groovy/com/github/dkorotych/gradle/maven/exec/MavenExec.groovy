package com.github.dkorotych.gradle.maven.exec

import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.TaskAction

/**
 * Implementation of the task to support the launch Maven.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExec extends AbstractExecTask<MavenExec> implements DefaultMavenExecSpec {

    /**
     * Create implementation of the task.
     */
    MavenExec() {
        super(MavenExec)
    }

    @Override
    @TaskAction
    protected void exec() {
        // build correct command line
        super.setCommandLine(commandLine)
        super.exec()
    }

    @Override
    void setCommandLine(Iterable<?> args) {
        throw new UnsupportedOperationException()
    }

    @Override
    void setCommandLine(Object... args) {
        throw new UnsupportedOperationException()
    }

    @Override
    MavenExec args(Object... args) {
        throw new UnsupportedOperationException()
    }

    @Override
    MavenExec args(Iterable<?> args) {
        throw new UnsupportedOperationException()
    }

    @Override
    MavenExec setArgs(Iterable<?> arguments) {
        throw new UnsupportedOperationException()
    }
}
