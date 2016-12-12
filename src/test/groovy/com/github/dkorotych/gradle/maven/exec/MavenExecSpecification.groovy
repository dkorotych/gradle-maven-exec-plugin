package com.github.dkorotych.gradle.maven.exec

import org.gradle.api.internal.file.IdentityFileResolver
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.ExecResult
import org.gradle.process.internal.DefaultExecAction
import org.gradle.process.internal.ExecException
import spock.lang.Specification

import static org.gradle.internal.os.OperatingSystem.*

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
class MavenExecSpecification extends Specification {
    private static final File userHome = new File(System.getProperty('user.home'))
    private static final File tmp = new File(System.getProperty('java.io.tmpdir'))

    def setOperatingSystem(OperatingSystem os) {
        if (WINDOWS == os) {
            asWindows()
        } else {
            asUnix()
        }
    }

    def asWindows() {
        System.setProperty('os.name', 'windows')
    }

    def asUnix() {
        System.setProperty('os.name', 'linux')
    }

    def operatingSystems() {
        [FREE_BSD, LINUX, MAC_OS, SOLARIS, WINDOWS]
    }

    def setMavenDirDataProvider() {
        def values = []
        operatingSystems().each { os ->
            [null, userHome, tmp].each { path ->
                values << [path, os, commandLine(path, os, 'clean', 'package')]
            }
        }
        values
    }

    def commandLine(File path, OperatingSystem os, String... goals) {
        def commandLine = []
        if (os == WINDOWS) {
            commandLine << 'cmd'
            commandLine << '/c'
        }
        commandLine << "${path ? path.absolutePath + '/' : ''}mvn${os == WINDOWS ? '.cmd' : ''}"
        commandLine.addAll(goals)
        commandLine
    }

    def setGoalsDataProvider() {
        def values = []
        operatingSystems().each { os ->
            values << [null, os, commandLine(null, os)]
            values << [['clean'], os, commandLine(null, os, 'clean')]
            values << [['package'], os, commandLine(null, os, 'package')]
            values << [['clean', 'package', 'site'], os, commandLine(null, os, 'clean', 'package', 'site')]
        }
        values
    }

    DefaultExecAction registerDefaultExecActionMock() {
        Mock(global: false, constructorArgs: [new IdentityFileResolver()], DefaultExecAction)
    }

    ExecResult getExecResult() {
        new ExecResult() {

            @Override
            int getExitValue() {
                return 0
            }

            @Override
            ExecResult assertNormalExitValue() throws ExecException {
                return this
            }

            @Override
            ExecResult rethrowFailure() throws ExecException {
                return this
            }
        }
    }
}
