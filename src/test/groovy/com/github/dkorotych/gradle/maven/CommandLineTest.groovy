package com.github.dkorotych.gradle.maven

import com.github.dkorotych.gradle.maven.exec.MavenExec
import com.github.dkorotych.gradle.maven.exec.MavenExecSpecification
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.spockframework.mock.IDefaultResponse
import org.spockframework.mock.IMockInvocation
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.environment.RestoreSystemProperties
import spock.util.mop.ConfineMetaClassChanges

import java.nio.file.Paths

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
class CommandLineTest extends Specification {
    @Shared
    Project project

    def setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.dkorotych.gradle-maven-exec'
    }

    @Unroll
    @RestoreSystemProperties
    @ConfineMetaClassChanges([MavenDescriptor])
    def "getCommandLine. Maven #version, #mavenDir, #os.familyName"() {
        setup:
        registerMavenDescriptorSpy(mavenDir, version)
        MavenExecSpecification.setOperatingSystem(os)
        MavenExec task = task {}

        when:
        task.goals 'clean', 'site'
        task.mavenDir mavenDir
        task.builder 'builderStrategy'
        task.legacyLocalRepository = true

        then:
        task.commandLine == commandLine

        cleanup:
        cleanupProject()

        where:
        [version, mavenDir, os, commandLine] << {
            def values = []
            ['2.2.1', '3.0.5'].each { version ->
                MavenExecSpecification.operatingSystems().each { os ->
                    MavenExecSpecification.mavenDirs().each { path ->
                        values << [version, path, os, MavenExecSpecification.commandLine(path, os, 'clean', 'site')]
                    }
                }
            }
            values
        }.call()
    }

    private MavenDescriptor registerMavenDescriptorSpy(File mavenDir, String version) {
        String directory = Paths.get(getClass().getResource("/fixtures/descriptor/$version").toURI())
                .toFile().absolutePath
        IDefaultResponse response = new IDefaultResponse() {
            @Override
            Object respond(IMockInvocation invocation) {
                return new MavenDescriptor(mavenDir) {
                    @Override
                    InputStream executeWithOption(String option) {
                        switch (option) {
                            case '--help':
                                return new ByteArrayInputStream(
                                        Paths.get(directory, 'help.txt')
                                                .toFile()
                                                .bytes)
                                break
                            case '--version':
                                return new ByteArrayInputStream(
                                        Paths.get(directory, 'version.txt')
                                                .toFile()
                                                .bytes)
                                break
                            default:
                                return super.executeWithOption(option)
                        }
                    }
                }
            }
        }
        GroovySpy(global: true, constructorArgs: [mavenDir], defaultResponse: response, MavenDescriptor)
    }

    private MavenExec task(Closure code) {
        project.task(["type": MavenExec], 'mavenExecTask').configure(code)
    }

    private void cleanupProject() {
        project.tasks.clear()
    }
}
