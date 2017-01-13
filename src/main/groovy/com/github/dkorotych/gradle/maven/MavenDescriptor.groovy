package com.github.dkorotych.gradle.maven

import com.github.dkorotych.gradle.maven.cli.MavenCommandBuilder
import groovy.transform.PackageScope
import org.gradle.process.internal.ExecHandleBuilder

import java.nio.charset.StandardCharsets
import java.util.regex.Matcher

/**
 * Description for the particular version of Maven placed in a certain directory.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 * @see MavenCommandBuilder
 */
class MavenDescriptor {
    private static final String CHARSET = StandardCharsets.US_ASCII.name()

    /**
     * Command line options for this version.
     */
    @Lazy
    Set<String> supportedOptions = {
        parseSupportedOptions(executeWithOption('--help'))
    }.call()
    /**
     * Application version.
     */
    @Lazy
    String version = {
        parseVersion(executeWithOption('--version'))
    }.call()

    private final File mavenDir

    /**
     * Create new descriptor.
     *
     * @param mavenDir Maven installation directory
     */
    MavenDescriptor(File mavenDir) {
        this.mavenDir = mavenDir
    }

    /**
     * Execute Maven with specific option.
     *
     * @param option Command line option
     * @return Command output
     */
    @PackageScope
    InputStream executeWithOption(String option) {
        List<String> commandLine = new MavenCommandBuilder(mavenDir).build()
        commandLine << option
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        new ExecHandleBuilder()
                .commandLine(commandLine)
                .setStandardOutput(outputStream)
                .workingDir(System.getProperty('java.io.tmpdir'))
                .build()
                .start()
                .waitForFinish()
        new ByteArrayInputStream(outputStream.toByteArray())
    }

    /**
     * Extract supported command line options from command output.
     *
     * @param stream Command output
     * @return Supported options or empty set
     */
    @PackageScope
    Set<String> parseSupportedOptions(InputStream stream) {
        Set<String> returnValue = []
        stream.eachLine CHARSET, {
            Matcher matcher = (it =~ /^\s-\w+,(--\S+).+$/)
            if (matcher.find()) {
                returnValue << matcher.group(1)
            }
        }
        returnValue
    }

    /**
     * Extract version of Maven application.
     *
     * @param stream Command output
     * @return Application version or empty string
     */
    @PackageScope
    String parseVersion(InputStream stream) {
        String returnValue = ''
        String pattern = /^\QApache Maven \E(\S+)(?:.+)?$/
        stream.readLines(CHARSET).findAll { it ==~ pattern }.each {
            Matcher matcher = (it =~ pattern)
            matcher.find()
            returnValue = matcher.group(1)
        }
        returnValue
    }
}
