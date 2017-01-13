package com.github.dkorotych.gradle.maven.cli

import com.github.dkorotych.gradle.maven.cli.bindable.PropertyBindable
import groovy.transform.IndexedProperty
import groovy.transform.ToString

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

/**
 * The Maven command line builder.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@ToString(includeNames = true, ignoreNulls = true)
@PropertyBindable
class MavenCli {
    /**
     * If project list is specified, also build projects required by the list
     */
    boolean alsoMake
    /**
     * If project list is specified, also build projects that depend on projects on the list
     */
    boolean alsoMakeDependents
    /**
     * Run in non-interactive (batch) mode
     */
    boolean batchMode
    /**
     * The id of the build strategy to use.
     */
    String builder
    /**
     * Fail the build if checksums don't match
     */
    boolean strictChecksums
    /**
     * Warn if checksums don't match
     */
    boolean laxChecksums
    /**
     * Define a system property
     */
    Map<String, String> define
    /**
     * Produce execution error messages
     */
    boolean errors
    /**
     * Encrypt master security password
     */
    String encryptMasterPassword
    /**
     * Encrypt server password
     */
    String encryptPassword
    /**
     * Force the use of an alternate POM file (or directory with pom.xml).
     */
    File file
    /**
     * Only fail the build afterwards; allow all non-impacted builds to continue
     */
    boolean failAtEnd
    /**
     * Stop at first failure in reactorized builds
     */
    boolean failFast
    /**
     * NEVER fail the build, regardless of project result
     */
    boolean failNever
    /**
     * Alternate path for the global settings file
     */
    File globalSettings
    /**
     * Alternate path for the global toolchains file
     */
    File globalToolchains
    /**
     * Log file where all build output will go.
     */
    File logFile
    /**
     * Use Maven 2 Legacy Local Repository behaviour, ie no use of _remote.repositories. Can also be activated by using
     * -Dmaven.legacyLocalRepo=true
     */
    boolean legacyLocalRepository
    /**
     * Do not recurse into sub-projects
     */
    boolean nonRecursive
    /**
     * Suppress SNAPSHOT updates
     */
    boolean noSnapshotUpdates
    /**
     * Work offline
     */
    boolean offline
    /**
     * Comma-delimited list of profiles to activate
     */
    @IndexedProperty
    String[] activateProfiles
    /**
     * Comma-delimited list of specified reactor projects to build instead of all projects. A project can be specified
     * by [groupId]:artifactId or by its relative path.
     */
    @IndexedProperty
    String[] projects
    /**
     * Quiet output - only show errors
     */
    boolean quiet
    /**
     * Resume reactor from specified project
     */
    String resumeFrom
    /**
     * Alternate path for the user settings file
     */
    File settings
    /**
     * Thread count, for instance 2.0C where C is core multiplied
     */
    String threads
    /**
     * Alternate path for the user toolchains file
     */
    File toolchains
    /**
     * Forces a check for missing releases and updated snapshots on remote repositories
     */
    boolean updateSnapshots
    /**
     * Produce execution debug output
     */
    boolean debug

    private final MavenCommandLineOptionsKeeper options
    private final MavenOptionsMapper mapper

    /**
     * Create command line builder.
     */
    MavenCli() {
        options = new MavenCommandLineOptionsKeeper()
        mapper = new MavenOptionsMapper()
        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            void propertyChange(PropertyChangeEvent event) {
                String option = mapper.getOptionName(event.propertyName)
                options.addOption(option, event.newValue)
            }
        })
    }

    /**
     * Set the list of options that are supported in a specific version of the Maven.
     *
     * @param supportedOptions list of options
     * @see MavenCommandLineOptionsKeeper#setSupportedOptions(java.util.Set)
     */
    void supportedOptions(Set<String> supportedOptions) {
        options.setSupportedOptions(supportedOptions)
    }

    /**
     * Build command line options.
     *
     * @return Command line options list
     */
    List<String> toCommandLine() {
        options.toCommandLine()
    }
}
