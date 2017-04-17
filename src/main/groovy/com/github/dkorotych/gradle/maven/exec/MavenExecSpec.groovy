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

import com.github.dkorotych.gradle.maven.cli.MavenCli
import org.gradle.process.BaseExecSpec

/**
 * Specifies options for launching a Maven process.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
interface MavenExecSpec extends BaseExecSpec {
    /**
     * Returns the Maven directory for the process.
     *
     * @return The Maven directory
     */
    File getMavenDir()

    /**
     * Sets the Maven directory for the process. The supplied argument is evaluated as per
     * {@link org.gradle.api.Project#file(Object)}.
     *
     * @param dir The Maven directory
     */
    void setMavenDir(File dir)

    /**
     * Sets the Maven directory for the process. The supplied argument is evaluated as per
     * {@link org.gradle.api.Project#file(Object)}.
     *
     * @param dir The Maven directory
     * @return this
     */
    MavenExecSpec mavenDir(File dir)

    /**
     * Adds goals for the Maven.
     *
     * @param goals goals for the Maven
     * @return this
     */
    MavenExecSpec goals(String... goals)

    /**
     * Adds goals for the Maven.
     *
     * @param goals goals for the Maven
     * @return this
     */
    MavenExecSpec goals(Iterable<String> goals)

    /**
     * Sets the goals for the Maven.
     *
     * @param goals goals for the Maven
     * @return this
     */
    MavenExecSpec setGoals(Iterable<String> goals)

    /**
     * Returns the goals for the Maven. Defaults to an empty list.
     *
     * @return goals for the Maven
     */
    Set<String> getGoals()

    /**
     * Command line options.
     *
     * @param options Options closure
     * @return this
     */
    MavenExecSpec options(@DelegatesTo(MavenCli) Closure<MavenCli> options)
}
