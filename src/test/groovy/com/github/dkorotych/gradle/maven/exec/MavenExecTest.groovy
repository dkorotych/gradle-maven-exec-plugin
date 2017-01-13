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
