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

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@SuppressWarnings(['MethodName', 'JUnitPublicNonTestMethod'])
class MavenOptionsMapperTest extends Specification {
    @Unroll
    "Convert '#propertyName' to '#expected'"() {
        setup:
        MavenOptionsMapper mapper = new MavenOptionsMapper()

        when: 'I convert a camel-case name to option name'
        String optionName = mapper.getOptionName(propertyName)

        then: 'The name is converted correctly'
        optionName == expected

        where:
        propertyName            | expected
        null                    | null
        ''                      | null
        '   \t\t\n\n    '       | null
        'alsoMake'              | '--also-make'
        'alsoMakeDependents'    | '--also-make-dependents'
        'batchMode'             | '--batch-mode'
        'builder'               | '--builder'
        'strictChecksums'       | '--strict-checksums'
        'laxChecksums'          | '--lax-checksums'
        'define'                | '--define'
        'errors'                | '--errors'
        'encryptMasterPassword' | '--encrypt-master-password'
        'encryptPassword'       | '--encrypt-password'
        'file'                  | '--file'
        'failAtEnd'             | '--fail-at-end'
        'failFast'              | '--fail-fast'
        'failNever'             | '--fail-never'
        'globalSettings'        | '--global-settings'
        'globalToolchains'      | '--global-toolchains'
        'logFile'               | '--log-file'
        'legacyLocalRepository' | '--legacy-local-repository'
        'nonRecursive'          | '--non-recursive'
        'noSnapshotUpdates'     | '--no-snapshot-updates'
        'offline'               | '--offline'
        'activateProfiles'      | '--activate-profiles'
        'projects'              | '--projects'
        'quiet'                 | '--quiet'
        'resumeFrom'            | '--resume-from'
        'settings'              | '--settings'
        'threads'               | '--threads'
        'toolchains'            | '--toolchains'
        'updateSnapshots'       | '--update-snapshots'
        'debug'                 | '--debug'
    }
}
