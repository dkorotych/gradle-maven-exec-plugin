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
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Unroll
import spock.util.environment.RestoreSystemProperties

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@RestoreSystemProperties
class DefaultMavenExecSpecTest extends MavenExecSpecification {
    @Shared
    Project project

    def setupSpec() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.dkorotych.gradle-maven-exec'
    }

    def "setExecutable should generate UnsupportedOperationException"() {
        when:
        task {
            executable 'echo'
        }

        then:
        thrown(UnsupportedOperationException.class)

        when:
        cleanupProject()
        task {
            setExecutable('echo')
        }

        then:
        thrown(UnsupportedOperationException.class)

        cleanup:
        cleanupProject()
    }

    @Unroll
    def "setMavenDir(#path). #os.familyName"() {
        setup:
        setOperatingSystem(os)
        registerMavenDescriptorMock(path, false)
        MavenExec task = task {
            goals = [
                    'clean', 'package'
            ]
        }

        when:
        task.setMavenDir(path)

        then:
        task.commandLine == commandLine

        when:
        task.mavenDir = path

        then:
        task.commandLine == commandLine

        when:
        task.mavenDir(path)

        then:
        task.commandLine == commandLine

        cleanup:
        cleanupProject()

        where:
        [path, os, commandLine] << setMavenDirDataProvider()
    }

    @Unroll
    def "setGoals(#goals). #os.familyName"() {
        setup:
        setOperatingSystem(os)
        registerMavenDescriptorMock()
        MavenExec task = task {}

        when:
        task.setGoals(goals)

        then:
        task.getGoals() == asSet(goals)
        task.commandLine == commandLine

        cleanup:
        cleanupProject()

        where:
        [goals, os, commandLine] << setGoalsDataProvider()
    }

    def "add goals"() {
        setup:
        asUnix()
        registerMavenDescriptorMock()
        MavenExec task = task {}

        when:
        task.goals((String[]) null)

        then:
        task.commandLine == ['mvn']

        when:
        task.goals()

        then:
        task.commandLine == ['mvn']

        when:
        task.goals((Iterable) null)

        then:
        task.commandLine == ['mvn']

        when:
        task.goals = ['package']

        then:
        task.commandLine == ['mvn', 'package']

        when:
        task.goals = ['clean']

        then:
        task.commandLine == ['mvn', 'clean']

        when:
        task.goals(['package', 'test'])

        then:
        task.commandLine == ['mvn', 'clean', 'package', 'test']

        when:
        task.goals('verify', 'install')

        then:
        task.commandLine == ['mvn', 'clean', 'package', 'test', 'verify', 'install']

        cleanup:
        cleanupProject()
    }

    def "options"() {
        when:
        asUnix()
        registerMavenDescriptorMock()
        MavenExec task = task {
            goals 'clean', 'package', 'site'
            options {
                threads = '1C'
                offline = true
                activateProfiles = ['development', 'site']
                define = [
                        'groupId'   : 'com.github.application',
                        'artifactId': 'parent'
                ]
            }
        }

        then:
        task.commandLine == ['mvn', '-DgroupId=com.github.application', '-DartifactId=parent',
                             '--threads', '1C', '--offline', '--activate-profiles', 'development,site',
                             'clean', 'package', 'site']

        cleanup:
        cleanupProject()
    }

    @Unroll
    def "getCommandLine. #path, #os.familyName"() {
        setup:
        setOperatingSystem(os)
        registerMavenDescriptorMock(path, false)
        MavenExec task = task {}

        when:
        task.goals 'clean', 'package'
        task.mavenDir path

        then:
        task.commandLine == commandLine

        cleanup:
        cleanupProject()

        where:
        [path, os, commandLine] << setMavenDirDataProvider()
    }

    MavenExec task(Closure code) {
        project.task(["type": MavenExec], 'mavenExecTask').configure(code)
    }

    def cleanupProject() {
        project.tasks.clear()
    }

    Set<String> asSet(List<String> goals) {
        if (goals) {
            return new HashSet<String>(goals)
        } else {
            return []
        }
    }
}
