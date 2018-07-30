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

import com.github.dkorotych.gradle.maven.cli.MavenCommandBuilder
import groovy.transform.PackageScope
import org.gradle.api.internal.file.IdentityFileResolver
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.internal.DefaultExecActionFactory
import org.gradle.process.internal.ExecAction

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
    private static final String VERSION_OPTION = '--version'

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
        parseVersion(executeWithOption(VERSION_OPTION))
    }.call()
    /**
     * Maven command line builder
     */
    @Lazy
    MavenCommandBuilder commandBuilder = {
        generateMavenCommandBuilder()
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
        execute(commandBuilder, option)
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

    /**
     * Generate correct command line builder object for support new and old Maven versions.
     *
     * @return Command line builder
     * @throws ExecuteException if it can't create command line builder
     */
    @PackageScope
    MavenCommandBuilder generateMavenCommandBuilder() {
        MavenCommandBuilder builder = new MavenCommandBuilder(mavenDir)
        try {
            execute(builder, VERSION_OPTION)
            return builder
        } catch (ExecuteException exception) {
            if (OperatingSystem.current().isWindows() && exception.message.contains('mvn.cmd')) {
                builder.oldVersion = true
                execute(builder, VERSION_OPTION)
                return builder
            }
            throw exception
        }
    }

    @SuppressWarnings('CatchThrowable')
    private static InputStream execute(MavenCommandBuilder commandBuilder, String option) throws ExecuteException {
        List<String> commandLine = commandBuilder.build()
        commandLine << option
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream()
        ExecAction execAction = new DefaultExecActionFactory(new IdentityFileResolver()).newExecAction()
        execAction.commandLine = commandLine
        execAction.standardOutput = outputStream
        execAction.errorOutput = errorStream
        execAction.workingDir = System.getProperty('java.io.tmpdir')
        try {
            execAction.execute()
        } catch (Throwable e) {
            String description = new ByteArrayInputStream(errorStream.toByteArray()).text
            if (description.trim().isEmpty()) {
                description = e.message
            }
            throw new ExecuteException(description, commandLine.join(' '), e)
        }
        new ByteArrayInputStream(outputStream.toByteArray())
    }
}
