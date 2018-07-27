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
package com.github.dkorotych.gradle.maven.cli

import com.github.dkorotych.gradle.maven.exec.MavenExecSpecification
import spock.lang.Unroll
import spock.util.environment.RestoreSystemProperties

import java.nio.file.Paths

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
@RestoreSystemProperties
class MavenCommandBuilderTest extends MavenExecSpecification {
    @Unroll
    def "mavenDir: #path, OS: #os.familyName, old version?: #oldVersion"() {
        setup:
        setOperatingSystem(os)
        MavenCommandBuilder builder = new MavenCommandBuilder(path)
        builder.oldVersion = oldVersion

        expect:
        builder.build() == command

        where:
        [path, os, oldVersion, command] << {
            AntBuilder ant = new AntBuilder()
            def values = []
            ['with', 'without'].each { dir ->
                def projectDir = File.createTempDir()
                ant.sequential {
                    copy(todir: projectDir) {
                        fileset(dir: Paths.get(getClass().getResource("/fixtures/wrapper/$dir").toURI()).toFile())
                    }
                }
                operatingSystems().each { os ->
                    [null, userHome, tmp, projectDir].each { path ->
                        def hasWrapper = dir == 'with' && path == projectDir
                        [false, true].each { oldVersion ->
                            values << [path, os, oldVersion, commandLine(path, os, oldVersion, hasWrapper)]
                        }
                    }
                }
            }
            values
        }.call()
    }
}
