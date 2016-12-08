package com.github.dkorotych.gradle.maven.exec

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecTest extends DefaultMavenExecSpecTest {

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
}
