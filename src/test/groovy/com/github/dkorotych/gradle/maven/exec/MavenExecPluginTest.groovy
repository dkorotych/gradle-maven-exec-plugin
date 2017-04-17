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

import org.gradle.api.Project
import org.gradle.process.ExecResult
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecPluginTest extends Specification {
    def "project has MavenExec task after apply plugin"() {
        setup:
        Project project = ProjectBuilder.builder().build()

        when:
        project.apply plugin: 'com.github.dkorotych.gradle-maven-exec'

        then:
        project.extensions.extraProperties.has(MavenExec.simpleName)
    }

    def "project has additional convention after apply plugin"() {
        setup:
        Project project = ProjectBuilder.builder().build()

        when:
        project.apply plugin: 'com.github.dkorotych.gradle-maven-exec'

        then:
        with(project.convention.plugins.get('mavenexec')) {
            it != null
            it.class == MavenExecPluginProjectConvention
            with(it.class.declaredMethods.find { it.name == 'mavenexec' }) {
                returnType == ExecResult
                it.parameterTypes as List == [Closure]
            }
        }
    }
}
