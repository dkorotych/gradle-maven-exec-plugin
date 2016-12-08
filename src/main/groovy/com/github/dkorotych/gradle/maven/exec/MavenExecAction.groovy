package com.github.dkorotych.gradle.maven.exec

import org.gradle.api.internal.file.IdentityFileResolver
import org.gradle.process.ExecResult
import org.gradle.process.internal.DefaultExecAction

/**
 * Implementation of the action to support the launch Maven.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecAction extends DefaultExecAction implements DefaultMavenExecSpec {
    /**
     * New instance of the action.
     */
    MavenExecAction() {
        super(new IdentityFileResolver())
    }

    @Override
    ExecResult execute() {
        setCommandLine(commandLine)
        super.execute()
    }
}
