/*
 * Copyright 2016 Dmitry Korotych
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    @SuppressWarnings('UnnecessarySetter')
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
    MavenExec commandLine(Object... arguments) {
        throw new UnsupportedOperationException()
    }

    @Override
    MavenExec commandLine(Iterable<?> args) {
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
