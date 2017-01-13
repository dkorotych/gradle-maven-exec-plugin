package com.github.dkorotych.gradle.maven.cli

import org.gradle.internal.os.OperatingSystem

/**
 * An builder that allows you to build a execution command, depending on the operating system and Maven installation
 * directory.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
class MavenCommandBuilder {
    private final boolean windows = OperatingSystem.current().isWindows()
    private final File mavenDir

    /**
     * Create new builder.
     *
     * @param mavenDir Maven installation directory
     */
    MavenCommandBuilder(File mavenDir) {
        this.mavenDir = mavenDir
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
            command = mavenDir.absolutePath + '/'
        }
        command += 'mvn'
        if (windows) {
            command += '.cmd'
        }
        parameters << command
        parameters
    }
}
