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
import org.gradle.api.internal.file.IdentityFileResolver
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.ExecResult
import org.gradle.process.internal.DefaultExecAction
import org.gradle.process.internal.ExecException
import org.gradle.util.GradleVersion
import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

import static org.gradle.internal.os.OperatingSystem.*

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
@RestoreSystemProperties
class MavenExecSpecification extends Specification {
    static final File userHome = new File(System.getProperty('user.home'))
    static final File tmp = new File(System.getProperty('java.io.tmpdir'))
    private static final GradleVersion GRADLE_3 = GradleVersion.version('3.0').baseVersion

    static void setOperatingSystem(OperatingSystem os) {
        resetCurrentOperatingSystem(os)
        if (WINDOWS == os) {
            asWindows()
        } else {
            asUnix()
        }
    }

    static asWindows() {
        resetCurrentOperatingSystem(current())
        System.setProperty('os.name', 'windows')
    }

    static asUnix() {
        resetCurrentOperatingSystem(current())
        System.setProperty('os.name', 'linux')
    }

    static List<OperatingSystem> operatingSystems() {
        [FREE_BSD, LINUX, MAC_OS, SOLARIS, WINDOWS]
    }

    static List<File> mavenDirs() {
        [null, userHome, tmp]
    }

    def setMavenDirDataProvider() {
        def values = []
        operatingSystems().each { os ->
            mavenDirs().each { path ->
                values << [path, os, commandLine(path, os, 'clean', 'package')]
            }
        }
        values
    }

    static List<String> commandLine(File path, OperatingSystem os, String... goals) {
        def commandLine = []
        if (os == WINDOWS) {
            commandLine << 'cmd'
            commandLine << '/c'
        }
        commandLine << "${path ? path.absolutePath + '/' : ''}mvn${os == WINDOWS ? '.cmd' : ''}"
        commandLine.addAll(goals)
        commandLine
    }

    static void resetCurrentOperatingSystem(OperatingSystem os) {
        if (GradleVersion.current().baseVersion.compareTo(GRADLE_3) >= 0) {
            os.metaClass.invokeMethod(os, "resetCurrent", null)
        }
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

    MavenDescriptor registerMavenDescriptorMock() {
        MavenDescriptor descriptor = GroovySpy(global: true, MavenDescriptor)
        descriptor.supportedOptions >> []
        descriptor.version >> '3.3.9'
        descriptor
    }

    static ExecResult getExecResult() {
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
