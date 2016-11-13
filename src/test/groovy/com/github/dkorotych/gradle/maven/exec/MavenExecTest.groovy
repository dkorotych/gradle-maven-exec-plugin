package com.github.dkorotych.gradle.maven.exec

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.internal.ExecAction
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.internal.os.OperatingSystem.*

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecTest extends Specification {
    private static final File userHome = new File(System.getProperty('user.home'))
    private static final File tmp = new File(System.getProperty('java.io.tmpdir'))

    @Shared
    private Project project

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

    @Unroll
    def "setMavenDir(#path). #os.familyName"() {
        setup:
        if (WINDOWS == os) {
            asWindows()
        } else {
            asUnix()
        }
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
        if (WINDOWS == os) {
            asWindows()
        } else {
            asUnix()
        }
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
        MavenExec task = task {}

        when:
        task.goals((String[]) null)

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
        if (WINDOWS == os) {
            asWindows()
        } else {
            asUnix()
        }
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

    private void asWindows() {
        System.setProperty('os.name', 'windows')
    }

    private void asUnix() {
        System.setProperty('os.name', 'linux')
    }

    private MavenExec task(Closure code) {
        project.task(["type": MavenExec], 'mavenExecTask').configure(code)
    }

    private void cleanupProject() {
        project.tasks.clear()
    }

    private def operatingSystems() {
        [FREE_BSD, LINUX, MAC_OS, SOLARIS, WINDOWS]
    }

    private def setMavenDirDataProvider() {
        def values = []
        operatingSystems().each { os ->
            [null, userHome, tmp].each { path ->
                values << [path, os, commandLine(path, os, 'clean', 'package')]
            }
        }
        values
    }

    private Set<String> asSet(List<String> goals) {
        if (goals) {
            return new HashSet<String>(goals)
        } else {
            return []
        }
    }

    private commandLine(File path, OperatingSystem os, String... goals) {
        def commandLine = []
        if (os == WINDOWS) {
            commandLine << 'cmd'
            commandLine << '/c'
        }
        commandLine << "${path ? path.absolutePath + '/' : ''}mvn${os == WINDOWS ? '.cmd' : ''}"
        commandLine.addAll(goals)
        commandLine
    }

    private def setGoalsDataProvider() {
        def values = []
        operatingSystems().each { os ->
            values << [null, os, commandLine(null, os)]
            values << [['clean'], os, commandLine(null, os, 'clean')]
            values << [['package'], os, commandLine(null, os, 'package')]
            values << [['clean', 'package', 'site'], os, commandLine(null, os, 'clean', 'package', 'site')]
        }
        values
    }
}
