/*
 * Copyright 2018 Dmitry Korotych
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
package com.github.dkorotych.gradle.maven

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Paths

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
class WorkWithAnotherGradleTest extends Specification {
    @Shared
    Project project
    @Shared
    String pluginVersion

    def setup() {
        pluginVersion = System.getProperty("pluginVersion")
        project = ProjectBuilder.builder().build()
    }

    @Unroll
    def "work with Gradle #version"() {
        setup:
        AntBuilder ant = new AntBuilder()
        ant.sequential {
            copy(todir: project.projectDir) {
                fileset(dir: Paths.get(getClass().getResource("/fixtures/versions").toURI()).toFile())
                filterset {
                    filter(token: "version", value: "${pluginVersion}")
                }
            }
        }
        wrapper(version).execute()
        JavaExec task = execute()

        when:
        task.execute()
        String output = project.file("output.txt").text

        then:
        task.state.failure == null
        output.contains("Validate work by Gradle: ${version}")
        output.contains("Plugin version: ${pluginVersion}")

        cleanup:
        cleanupProject()

        where:
        version << {
            def versions = ['3.0', '4.0', '4.5.1', '4.6', '4.10.2']
            if (JavaVersion.current().isJava8Compatible()) {
                versions.addAll(['5.0', '5.1', '5.2.1', '5.3.1', '5.4.1', '5.5.1', '5.6.4', '6.0.1', '6.1.1'])
            }
            versions
        }.call()
    }

    private Wrapper wrapper(String version) {
        project.task(["type": Wrapper], 'wrapper') {
            gradleVersion = version
        }
    }

    private JavaExec execute() {
        OutputStream output = project.file("output.txt").newOutputStream()
        project.task(["type": JavaExec], 'execute') {
            classpath = project.files("gradle/wrapper/gradle-wrapper.jar")
            workingDir = project.projectDir
            main = "org.gradle.wrapper.GradleWrapperMain"
            args = ["--no-daemon", "tests"]
            standardOutput = output
        }
    }

    private void cleanupProject() {
        project.tasks.clear()
        project.projectDir.delete()
    }
}
