package com.github.dkorotych.gradle.maven

import com.github.dkorotych.gradle.maven.exec.MavenExecSpecification
import org.gradle.process.internal.ExecHandle
import org.gradle.process.internal.ExecHandleBuilder
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.environment.RestoreSystemProperties
import spock.util.mop.ConfineMetaClassChanges

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
class MavenDescriptorTest extends Specification {

    @Unroll
    @ConfineMetaClassChanges([ExecHandleBuilder, MavenDescriptor, ExecHandle])
    @RestoreSystemProperties
    def "executeWithOption #mavenDir"() {
        setup:
        MavenExecSpecification.asUnix()
        ProjectBuilder.builder().build()
        ExecHandleBuilder handleBuilder = GroovySpy(global: true, ExecHandleBuilder)
        ExecHandle handle = Stub(ExecHandle)
        handleBuilder.build() >> handle
        handle.start() >> handle
        MavenDescriptor descriptor = Spy(constructorArgs: [mavenDir], MavenDescriptor)

        when:
        descriptor.supportedOptions

        then:
        1 * descriptor.executeWithOption('--help')
        1 * descriptor.parseSupportedOptions(_)
        1 * handleBuilder.commandLine(["${mavenDir ? mavenDir.absolutePath + '/' : ''}mvn", '--help'])
        1 * handleBuilder.setStandardOutput(_ as ByteArrayOutputStream)
        1 * handleBuilder.workingDir(System.getProperty('java.io.tmpdir'))

        when:
        descriptor.supportedOptions

        then:
        0 * descriptor.executeWithOption(_)
        0 * descriptor.parseSupportedOptions(_)
        0 * handleBuilder._
        0 * handle._

        when:
        descriptor.version

        then:
        1 * descriptor.executeWithOption('--version')
        1 * descriptor.parseVersion(_)
        1 * handleBuilder.commandLine(["${mavenDir ? mavenDir.absolutePath + '/' : ''}mvn", '--version'])
        1 * handleBuilder.setStandardOutput(_ as ByteArrayOutputStream)
        1 * handleBuilder.workingDir(System.getProperty('java.io.tmpdir'))

        when:
        descriptor.version

        then:
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
                        it.toFile().name,
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
                        it.toFile().name,
                        getVersion(it)
                ]
            }
            values
        }.call()
    }

    private Stream<Path> descriptorFixtures() {
        Files.list(Paths.get(getClass().getResource('/fixtures/descriptor').toURI()))
                .filter {
            it.toFile().isDirectory()
        }
    }

    private String getVersion(Path version) {
        getText(version, 'version.txt')
    }

    private String getHelp(Path version) {
        getText(version, 'help.txt')
    }

    private String getOptions(Path version) {
        getText(version, 'options.txt')
    }

    private String getText(Path version, String fileName) {
        Paths.get(version.toAbsolutePath().toString(), fileName).text
    }
}
