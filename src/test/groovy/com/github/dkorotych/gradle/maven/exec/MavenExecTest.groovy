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

import spock.lang.Unroll

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecTest extends DefaultMavenExecSpecTest {

    def "setCommandLine should generate UnsupportedOperationException"() {
        when:
        task {
            setCommandLine(['echo', '--help'])
        }

        then:
        thrown(UnsupportedOperationException.class)

        when:
        cleanupProject()
        task {
            setCommandLine('echo', '--help')
        }

        then:
        thrown(UnsupportedOperationException.class)

        when:
        cleanupProject()
        task {
            commandLine = ['echo', '--help']
        }

        then:
        thrown(UnsupportedOperationException.class)

        when:
        cleanupProject()
        task {
            commandLine(['echo', '--help'])
        }

        then:
        thrown(UnsupportedOperationException.class)

        when:
        cleanupProject()
        task {
            commandLine('echo', '--help')
        }

        then:
        thrown(UnsupportedOperationException.class)

        cleanup:
        cleanupProject()
    }

    def "setArgs should generate UnsupportedOperationException"() {
        when:
        task {
            args('echo', '--help')
        }

        then:
        thrown(UnsupportedOperationException.class)

        when:
        cleanupProject()
        task {
            args(['echo', '--help'])
        }

        then:
        thrown(UnsupportedOperationException.class)

        when:
        cleanupProject()
        task {
            setArgs(['echo', '--help'])
        }

        then:
        thrown(UnsupportedOperationException.class)

        cleanup:
        cleanupProject()
    }

    @Unroll
    def "exec(). #os.familyName, #goals"() {
        setup:
        setOperatingSystem(os)
        registerMavenDescriptorMock()

        when:
        def task = task {}
        task.metaClass.exec = {}
        task.setGoals(goals)
        task.exec()

        then:
        task.commandLine == commandLine

        cleanup:
        cleanupProject()

        where:
        [goals, os, commandLine] << setGoalsDataProvider()
    }
}
