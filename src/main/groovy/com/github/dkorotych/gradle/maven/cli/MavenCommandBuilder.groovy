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

import org.gradle.internal.os.OperatingSystem

/**
 * An builder that allows you to build a execution command, depending on the operating system and Maven installation
 * directory.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
class MavenCommandBuilder {
    boolean oldVersion = false

    private final boolean windows = OperatingSystem.current().isWindows()
    private final File mavenDir
    private final boolean hasWrapper

    /**
     * Create new builder.
     *
     * @param mavenDir Maven installation directory
     */
    MavenCommandBuilder(File mavenDir) {
        this.mavenDir = mavenDir
        if (mavenDir) {
            hasWrapper = mavenDir.listFiles({
                !it.directory && it.name ==~ /(?i)^mvnw(?:\.cmd)?$/
            } as FileFilter).any()
        } else {
            hasWrapper = false
        }
    }

    /**
     * Build execution command.
     *
     * @return command line
     */
    List<String> build() {
        List<String> parameters = []
        if (windows) {
            parameters << 'cmd'
            parameters << '/c'
        }
        String command = ''
        if (mavenDir != null) {
            command = mavenDir.absolutePath + File.separatorChar
        }
        command += "mvn${hasWrapper ? 'w' : ''}"
        if (windows) {
            String extension = '.cmd'
            if (hasWrapper) {
                command += extension
            } else {
                if (oldVersion) {
                    command += '.bat'
                } else {
                    command += extension
                }
            }
        }
        parameters << command
        parameters
    }
}
