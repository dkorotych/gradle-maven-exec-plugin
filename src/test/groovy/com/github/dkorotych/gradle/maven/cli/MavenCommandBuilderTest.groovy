package com.github.dkorotych.gradle.maven.cli

import com.github.dkorotych.gradle.maven.exec.MavenExecSpecification
import spock.lang.Unroll
import spock.util.environment.RestoreSystemProperties

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
@RestoreSystemProperties
class MavenCommandBuilderTest extends MavenExecSpecification {
    @Unroll
    def "mavenDir: #path, OS: #os.familyName"() {
        setup:
        setOperatingSystem(os)
        MavenCommandBuilder builder = new MavenCommandBuilder(path)

        expect:
        builder.build() == command

        where:
        [path, os, command] << {
            def values = []
            operatingSystems().each { os ->
                [null, userHome, tmp].each { path ->
                    values << [path, os, commandLine(path, os)]
                }
            }
            values
        }.call()
    }
}
