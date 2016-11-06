package com.github.dkorotych.gradle.maven.exec

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecPluginTest extends Specification {
    def "project has MavenExec task after apply plugin"() {
        setup:
        Project project = ProjectBuilder.builder().build()

        when:
        project.apply plugin: 'com.github.dkorotych.gradle-maven-exec'

        then:
        project.extensions.extraProperties.has(MavenExec.simpleName)
    }
}
