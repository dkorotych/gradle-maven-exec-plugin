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

import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenCliTest extends Specification {
    @Unroll
    def "boolean '#propertyName' = #value is #commandLine"() {
        setup:
        MavenCli options = new MavenCli()

        when:
        options[propertyName] = value

        then:
        commandLine == options.toCommandLine()

        where:
        [propertyName, value, commandLine] << getBooleanPropertiesDataProvider()
    }

    private static getBooleanPropertiesDataProvider() {
        def values = []
        [
                'alsoMake'             : 'also-make',
                'alsoMakeDependents'   : 'also-make-dependents',
                'batchMode'            : 'batch-mode',
                'strictChecksums'      : 'strict-checksums',
                'laxChecksums'         : 'lax-checksums',
                'errors'               : 'errors',
                'failAtEnd'            : 'fail-at-end',
                'failFast'             : 'fail-fast',
                'failNever'            : 'fail-never',
                'legacyLocalRepository': 'legacy-local-repository',
                'nonRecursive'         : 'non-recursive',
                'noSnapshotUpdates'    : 'no-snapshot-updates',
                'offline'              : 'offline',
                'quiet'                : 'quiet',
                'updateSnapshots'      : 'update-snapshots',
                'debug'                : 'debug'
        ].each {
            values << [it.key, true, ["--${it.value}"]]
            values << [it.key, false, []]
        }
        values
    }

    @Unroll
    def "String '#propertyName' = #value is #commandLine"() {
        setup:
        MavenCli options = new MavenCli()

        when:
        options[propertyName] = value

        then:
        commandLine == options.toCommandLine()

        where:
        [propertyName, value, commandLine] << getStringPropertiesDataProvider()
    }

    private static getStringPropertiesDataProvider() {
        def values = []
        [
                'builder'              : 'builder',
                'encryptMasterPassword': 'encrypt-master-password',
                'encryptPassword'      : 'encrypt-password',
                'resumeFrom'           : 'resume-from',
                'threads'              : 'threads'
        ].each {
            String value = RandomStringUtils.randomAlphanumeric(10)
            values << [it.key, '', []]
            values << [it.key, '   \t\n  ', []]
            values << [it.key, value, ["--${it.value}", value]]
        }
        values
    }

    @Unroll
    def "File '#propertyName' = #value is #commandLine"() {
        setup:
        MavenCli options = new MavenCli()

        when:
        options[propertyName] = value

        then:
        commandLine == options.toCommandLine()

        where:
        [propertyName, value, commandLine] << getFilePropertiesDataProvider()
    }

    private static getFilePropertiesDataProvider() {
        File userHome = new File(System.getProperty('user.home'))
        File tmp = new File(System.getProperty('java.io.tmpdir'))
        def values = []
        [
                'file'            : 'file',
                'globalSettings'  : 'global-settings',
                'globalToolchains': 'global-toolchains',
                'logFile'         : 'log-file',
                'settings'        : 'settings',
                'toolchains'      : 'toolchains'
        ].each {
            values << [it.key, userHome, ["--${it.value}", userHome.absolutePath]]
            values << [it.key, tmp, ["--${it.value}", tmp.absolutePath]]
        }
        values
    }

    def "Map 'define' = #value is #commandLine"() {
        setup:
        MavenCli options = new MavenCli()

        when:
        options.define = value

        then:
        commandLine == options.toCommandLine()

        where:
        value                                  | commandLine
        [:]                                    | []
        ['first': 'value', 'second': 'test']   | ['-Dfirst=value', '-Dsecond=test']
        ['first': 'value', 'second': null]     | ['-Dfirst=value']
        ['first': 'value', 'second': '    \t'] | ['-Dfirst=value']
        ['  ': 'value', 'second': 'test']      | ['-Dsecond=test']
        [' \t ': 'value']                      | []
    }

    @Unroll
    def "String[] '#propertyName' = #value is #commandLine"() {
        setup:
        MavenCli options = new MavenCli()

        when:
        options[propertyName] = value as List<String>

        then:
        commandLine == options.toCommandLine()

        where:
        [propertyName, value, commandLine] << getStringsPropertiesDataProvider()
    }

    private static getStringsPropertiesDataProvider() {
        def values = []
        [
                'activateProfiles': 'activate-profiles',
                'projects'        : 'projects'
        ].each {
            String first = RandomStringUtils.randomAlphanumeric(10)
            String second = RandomStringUtils.randomAlphanumeric(10)
            values << [it.key, [], []]
            values << [it.key, ['   \t\n  '], []]
            values << [it.key, ['   \t\n  ', '', '    '], []]
            values << [it.key, [first], ["--${it.value}", first]]
            values << [it.key, [null, second, '', first, ' '], ["--${it.value}", "${second},${first}"]]
        }
        values
    }
}
