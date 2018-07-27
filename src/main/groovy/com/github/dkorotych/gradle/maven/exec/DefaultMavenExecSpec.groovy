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

import com.github.dkorotych.gradle.maven.MavenDescriptor
import com.github.dkorotych.gradle.maven.cli.MavenCli
import com.github.dkorotych.gradle.maven.cli.MavenCommandBuilder
import groovy.transform.Memoized
import groovy.transform.PackageScope
import groovy.transform.TypeChecked

/**
 * Default implementation for specifies options for launching a Maven process.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@PackageScope
trait DefaultMavenExecSpec implements MavenExecSpec {
    @Delegate(
            excludes = [
                    'addPropertyChangeListener',
                    'removePropertyChangeListener',
                    'firePropertyChange',
                    'getPropertyChangeListeners',
            ],
            interfaces = false)
    private final MavenCli mavenCli = new MavenCli()
    private final Set<String> goalSet = []
    private File installDir

    @Override
    File getMavenDir() {
        installDir
    }

    @Override
    void setMavenDir(File dir) {
        installDir = dir
    }

    @Override
    MavenExecSpec mavenDir(File dir) {
        installDir = dir
        this
    }

    @Override
    MavenExecSpec goals(String... goals) {
        if (goals) {
            goalSet.addAll(goals)
        }
        this
    }

    @Override
    MavenExecSpec goals(Iterable<String> goals) {
        if (goals) {
            goalSet.addAll(goals)
        }
        this
    }

    @Override
    MavenExecSpec setGoals(Iterable<String> goals) {
        if (goals) {
            goalSet.clear()
            goalSet.addAll(goals)
        }
        this
    }

    @Override
    Set<String> getGoals() {
        goalSet
    }

    @Override
    List<String> getCommandLine() {
        prepareCommandLine()
    }

    @TypeChecked
    @Override
    MavenExecSpec options(@DelegatesTo(MavenCli) Closure options) {
        this.mavenCli.with options
        this
    }

    @Override
    void setExecutable(Object executable) {
        throw new UnsupportedOperationException()
    }

    void setExecutable(String executable) {
        throw new UnsupportedOperationException()
    }

    @Override
    MavenExec executable(Object executable) {
        throw new UnsupportedOperationException()
    }

    private List<String> prepareCommandLine() {
        List<String> parameters = []
        parameters.addAll(getMavenCommandBuilder(mavenDir).build())
        mavenCli.supportedOptions(getSupportedOptions(mavenDir))
        parameters.addAll(mavenCli.toCommandLine())
        parameters.addAll(goals)
        parameters
    }

    @Memoized
    private MavenDescriptor getDescriptor(File mavenDir) {
        new MavenDescriptor(mavenDir)
    }

    @Memoized
    private Set<String> getSupportedOptions(File mavenDir) {
        getDescriptor(mavenDir).supportedOptions
    }

    @Memoized
    private MavenCommandBuilder getMavenCommandBuilder(File mavenDir) {
        getDescriptor(mavenDir).commandBuilder
    }
}
