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
package com.github.dkorotych.gradle.maven

import com.github.dkorotych.gradle.maven.exec.MavenExecSpecification
import org.gradle.api.internal.file.IdentityFileResolver
import org.gradle.process.internal.*
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.environment.RestoreSystemProperties
import spock.util.mop.ConfineMetaClassChanges

import java.nio.file.Paths

import static org.gradle.internal.os.OperatingSystem.LINUX
import static org.gradle.internal.os.OperatingSystem.WINDOWS

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
class MavenDescriptorTest extends Specification {

    @Unroll
    @ConfineMetaClassChanges([ExecHandleBuilder, DefaultExecActionFactory, MavenDescriptor, ExecHandle])
    @RestoreSystemProperties
    def "executeWithOption #mavenDir"() {
        setup:
        ProjectBuilder.builder().build()
        MavenExecSpecification.asUnix()
        DefaultExecAction handleBuilder = spyAction()
        spyFactory(handleBuilder)
        ExecHandle handle = Stub(ExecHandle)
        handleBuilder.build() >> handle
        handle.start() >> handle
        handleBuilder.ignoreExitValue >> true
        MavenDescriptor descriptor = Spy(constructorArgs: [mavenDir], MavenDescriptor)

        when:
        descriptor.supportedOptions

        then:
        1 * descriptor.commandBuilder
        1 * descriptor.executeWithOption('--help')
        1 * descriptor.parseSupportedOptions(_)
        1 * handleBuilder.commandLine(["${mavenDir ? mavenDir.absolutePath + File.separatorChar : ''}mvn", '--help'])
        2 * handleBuilder.setStandardOutput(_ as ByteArrayOutputStream)
        2 * handleBuilder.setErrorOutput(_ as ByteArrayOutputStream)
        2 * handleBuilder.setWorkingDir(System.getProperty('java.io.tmpdir'))

        when:
        descriptor.supportedOptions

        then:
        0 * descriptor.commandBuilder
        0 * descriptor.executeWithOption(_)
        0 * descriptor.parseSupportedOptions(_)
        0 * handleBuilder._
        0 * handle._

        when:
        descriptor.version

        then:
        1 * descriptor.commandBuilder
        1 * descriptor.executeWithOption('--version')
        1 * descriptor.parseVersion(_)
        1 * handleBuilder.commandLine(["${mavenDir ? mavenDir.absolutePath + File.separatorChar : ''}mvn", '--version'])
        1 * handleBuilder.setStandardOutput(_ as ByteArrayOutputStream)
        1 * handleBuilder.setErrorOutput(_ as ByteArrayOutputStream)
        1 * handleBuilder.setWorkingDir(System.getProperty('java.io.tmpdir'))

        when:
        descriptor.version

        then:
        0 * descriptor.commandBuilder
        0 * descriptor.executeWithOption(_)
        0 * descriptor.parseVersion(_)
        0 * handleBuilder._
        0 * handle._

        where:
        mavenDir << MavenExecSpecification.mavenDirs()
    }

    @Unroll
    @ConfineMetaClassChanges([ExecHandleBuilder, DefaultExecActionFactory, MavenDescriptor, ExecHandle])
    @RestoreSystemProperties
    def "generateMavenCommandBuilder. mavenDir: #mavenDir, OS: #os.familyName"() {
        setup:
        ProjectBuilder.builder().build()
        MavenExecSpecification.setOperatingSystem(os)
        DefaultExecAction handleBuilder = spyAction()
        spyFactory(handleBuilder)
        ExecHandle handle = Stub(ExecHandle)
        handleBuilder.build() >> handle
        handle.start() >> handle
        handleBuilder.ignoreExitValue >> true
        MavenDescriptor descriptor = Spy(constructorArgs: [mavenDir], MavenDescriptor)
        command << '--version'

        when:
        descriptor.commandBuilder

        then:
        1 * descriptor.commandBuilder
        1 * descriptor.generateMavenCommandBuilder()
        1 * handleBuilder.setCommandLine(command)
        1 * handleBuilder.setStandardOutput(_ as ByteArrayOutputStream)
        1 * handleBuilder.setErrorOutput(_ as ByteArrayOutputStream)
        1 * handleBuilder.setWorkingDir(System.getProperty('java.io.tmpdir'))
        1 * handleBuilder.execute()
        0 * descriptor.parseSupportedOptions(_)

        when:
        descriptor.commandBuilder

        then:
        1 * descriptor.commandBuilder
        0 * descriptor._
        0 * handleBuilder._
        0 * handle._

        where:
        [mavenDir, os, command] << {
            def values = []
            MavenExecSpecification.operatingSystems().each { os ->
                [null, MavenExecSpecification.userHome, MavenExecSpecification.tmp].each { path ->
                    values << [path, os, MavenExecSpecification.commandLine(path, os)]
                }
            }
            values
        }.call()
    }

    @Unroll
    @ConfineMetaClassChanges([ExecHandleBuilder, DefaultExecActionFactory, MavenDescriptor, ExecHandle])
    @RestoreSystemProperties
    def "generateMavenCommandBuilder for old Maven versions on Windows. mavenDir: #mavenDir"() {
        setup:
        ProjectBuilder.builder().build()
        MavenExecSpecification.asWindows()
        DefaultExecAction handleBuilder = spyAction()
        spyFactory(handleBuilder)
        ExecHandle handle = Stub(ExecHandle)
        handleBuilder.build() >> handle
        handle.start() >> { throw new ExecException("mvn.cmd") } >> handle
        handleBuilder.ignoreExitValue >> true
        MavenDescriptor descriptor = Spy(constructorArgs: [mavenDir], MavenDescriptor)
        command << '--version'

        when:
        descriptor.commandBuilder

        then:
        1 * handleBuilder.setCommandLine(command)
        2 * handleBuilder.execute()

        where:
        [mavenDir, command] << {
            def values = []
            [null, MavenExecSpecification.userHome, MavenExecSpecification.tmp].each { path ->
                values << [path, MavenExecSpecification.commandLine(path, WINDOWS, true, false)]
            }
            values
        }.call()
    }

    @Unroll
    @ConfineMetaClassChanges([ExecHandleBuilder, DefaultExecActionFactory, MavenDescriptor, ExecHandle])
    @RestoreSystemProperties
    def "exception. mavenDir: #mavenDir, command: #command"() {
        setup:
        ProjectBuilder.builder().build()
        MavenExecSpecification.asUnix()
        DefaultExecAction handleBuilder = spyAction()
        spyFactory(handleBuilder)
        ExecHandle handle = Stub(ExecHandle)
        handleBuilder.build() >> handle
        handle.start() >> {
            throw new ExecException("mvn.cmd")
        }
        MavenDescriptor descriptor = Spy(constructorArgs: [mavenDir], MavenDescriptor)
        command << '--version'

        when:
        descriptor.commandBuilder

        then:
        ExecuteException exception = thrown(ExecuteException)
        exception.commandLine == command.join(' ')

        where:
        [mavenDir, command] << {
            def values = []
            [null, MavenExecSpecification.userHome, MavenExecSpecification.tmp].each { path ->
                values << [path, MavenExecSpecification.commandLine(path, LINUX)]
            }
            values
        }.call()
    }

    @Unroll
    def "getSupportedOptions(#version)"() {
        setup:
        MavenDescriptor descriptor = Spy(MavenDescriptor)
        descriptor.executeWithOption('--help') >> new ByteArrayInputStream(text.bytes)

        expect:
        descriptor.supportedOptions == options

        where:
        [version, text, options] << {
            def values = []
            descriptorFixtures().each {
                values << [
                        it.name,
                        getHelp(it),
                        getOptions(it).readLines() as LinkedHashSet
                ]
            }
            values
        }.call()
    }

    @Unroll
    def "getVersion(#version)"() {
        setup:
        MavenDescriptor descriptor = Spy(MavenDescriptor)
        descriptor.executeWithOption('--version') >> new ByteArrayInputStream(text.bytes)

        expect:
        descriptor.version == version

        where:
        [version, text] << {
            def values = []
            descriptorFixtures().each {
                values << [
                        it.name,
                        getVersion(it)
                ]
            }
            values
        }.call()
    }

    static List<File> descriptorFixtures() {
        Paths.get(getClass().getResource('/fixtures/descriptor').toURI())
                .toFile()
                .listFiles()
                .findAll { it.isDirectory() }
    }

    private static String getVersion(File file) {
        getText(file, 'version.txt')
    }

    private static String getHelp(File file) {
        getText(file, 'help.txt')
    }

    private static String getOptions(File file) {
        getText(file, 'options.txt')
    }

    private static String getText(File file, String fileName) {
        Paths.get(file.absolutePath, fileName).text
    }

    private DefaultExecAction spyAction() {
        GroovySpy(constructorArgs: MavenExecSpecification.createDefaultExecActionConstructorArguments(),
                DefaultExecAction)
    }

    private void spyFactory(DefaultExecAction handleBuilder) {
        DefaultExecActionFactory factory = GroovySpy(global: true,
                constructorArgs: [new IdentityFileResolver()], DefaultExecActionFactory)
        factory.newExecAction() >> handleBuilder
    }
}
