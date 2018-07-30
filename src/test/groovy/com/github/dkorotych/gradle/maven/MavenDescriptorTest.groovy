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
import org.gradle.process.internal.DefaultExecAction
import org.gradle.process.internal.DefaultExecActionFactory
import org.gradle.process.internal.ExecHandle
import org.gradle.process.internal.ExecHandleBuilder
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.environment.RestoreSystemProperties
import spock.util.mop.ConfineMetaClassChanges

import java.nio.file.Paths

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
class MavenDescriptorTest extends Specification {

    @Unroll
    @ConfineMetaClassChanges([ExecHandleBuilder, MavenDescriptor, ExecHandle])
    @RestoreSystemProperties
    def "executeWithOption #mavenDir"() {
        setup:
        ProjectBuilder.builder().build()
        MavenExecSpecification.asUnix()
        ExecHandleBuilder handleBuilder = GroovySpy(
                constructorArgs: MavenExecSpecification.createDefaultExecActionConstructorArguments(), DefaultExecAction)
        DefaultExecActionFactory factory = GroovySpy(global: true,
                constructorArgs: [new IdentityFileResolver()], DefaultExecActionFactory)
        factory.newExecAction() >> handleBuilder
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
        1 * handleBuilder.commandLine(["${mavenDir ? mavenDir.absolutePath + '/' : ''}mvn", '--help'])
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
        1 * handleBuilder.commandLine(["${mavenDir ? mavenDir.absolutePath + '/' : ''}mvn", '--version'])
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
}
