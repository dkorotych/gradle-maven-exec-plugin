package com.github.dkorotych.gradle.maven.cli

import groovy.transform.PackageScope

/**
 * Converter between internal property names and command line options for Maven.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@PackageScope
class MavenOptionsMapper {
    private final Map<String, String> optionsMapper

    /**
     * Create new converter.
     */
    @SuppressWarnings('SpaceAroundMapEntryColon')
    MavenOptionsMapper() {
        optionsMapper = [
                'alsoMake'             : 'also-make',
                'alsoMakeDependents'   : 'also-make-dependents',
                'batchMode'            : 'batch-mode',
                'strictChecksums'      : 'strict-checksums',
                'laxChecksums'         : 'lax-checksums',
                'encryptMasterPassword': 'encrypt-master-password',
                'encryptPassword'      : 'encrypt-password',
                'failAtEnd'            : 'fail-at-end',
                'failFast'             : 'fail-fast',
                'failNever'            : 'fail-never',
                'globalSettings'       : 'global-settings',
                'globalToolchains'     : 'global-toolchains',
                'logFile'              : 'log-file',
                'legacyLocalRepository': 'legacy-local-repository',
                'nonRecursive'         : 'non-recursive',
                'noSnapshotUpdates'    : 'no-snapshot-updates',
                'activateProfiles'     : 'activate-profiles',
                'resumeFrom'           : 'resume-from',
                'updateSnapshots'      : 'update-snapshots',
        ]
    }

    /**
     * Convert internal property name to Maven command line option.
     *
     * @param propertyName Property name
     * @return Command line option
     */
    String getOptionName(String propertyName) {
        if (propertyName) {
            String name = propertyName.trim()
            if (!name.empty) {
                return "--${optionsMapper.get(name, name)}"
            }
        }
        null
    }
}
