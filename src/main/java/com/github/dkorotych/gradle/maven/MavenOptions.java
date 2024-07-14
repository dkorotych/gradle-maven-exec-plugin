/**
 * Copyright 2022 Dmitry Korotych
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.dkorotych.gradle.maven;

import java.io.File;
import java.util.Map;

/**
 * Configuration options for any supported version of Maven.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@SuppressWarnings({
        "checkstyle:MethodCount",
        "PMD.ExcessivePublicCount",
        "PMD.TooManyMethods"
})
public interface MavenOptions {
    /**
     * If project list is specified, also build projects required by the list.
     *
     * @return Option value
     */
    boolean isAlsoMake();

    /**
     * If project list is specified, also build projects required by the list.
     *
     * @param alsoMake Option value
     */
    void setAlsoMake(boolean alsoMake);

    /**
     * If project list is specified, also build projects required by the list.
     *
     * @return Option value
     */
    default boolean alsoMake() {
        return isAlsoMake();
    }

    /**
     * If project list is specified, also build projects required by the list.
     *
     * @param alsoMake Option value
     */
    default void alsoMake(final boolean alsoMake) {
        setAlsoMake(alsoMake);
    }

    /**
     * If project list is specified, also build projects that depend on projects on the list.
     *
     * @return Option value
     */
    boolean isAlsoMakeDependents();

    /**
     * If project list is specified, also build projects that depend on projects on the list.
     *
     * @param alsoMakeDependents Option value
     */
    void setAlsoMakeDependents(boolean alsoMakeDependents);

    /**
     * If project list is specified, also build projects that depend on projects on the list.
     *
     * @return Option value
     */
    default boolean alsoMakeDependents() {
        return isAlsoMakeDependents();
    }

    /**
     * If project list is specified, also build projects that depend on projects on the list.
     *
     * @param alsoMakeDependents Option value
     */
    default void alsoMakeDependents(final boolean alsoMakeDependents) {
        setAlsoMakeDependents(alsoMakeDependents);
    }

    /**
     * Run in non-interactive (batch) mode (disables output color).
     *
     * @return Option value
     */
    boolean isBatchMode();

    /**
     * Run in non-interactive (batch) mode (disables output color).
     *
     * @param batchMode Option value
     */
    void setBatchMode(boolean batchMode);

    /**
     * Run in non-interactive (batch) mode (disables output color).
     *
     * @return Option value
     */
    default boolean batchMode() {
        return isBatchMode();
    }

    /**
     * Run in non-interactive (batch) mode (disables output color).
     *
     * @param batchMode Option value
     */
    default void batchMode(final boolean batchMode) {
        setBatchMode(batchMode);
    }

    /**
     * The id of the build strategy to use.
     *
     * @return Option value
     */
    String getBuilder();

    /**
     * The id of the build strategy to use.
     *
     * @param builder Option value
     */
    void setBuilder(String builder);

    /**
     * The id of the build strategy to use.
     *
     * @return Option value
     */
    default String builder() {
        return getBuilder();
    }

    /**
     * The id of the build strategy to use.
     *
     * @param builder Option value
     */
    default void builder(final String builder) {
        setBuilder(builder);
    }

    /**
     * Fail the build if checksums don't match.
     *
     * @return Option value
     */
    boolean isStrictChecksums();

    /**
     * Fail the build if checksums don't match.
     *
     * @param strictChecksums Option value
     */
    void setStrictChecksums(boolean strictChecksums);

    /**
     * Fail the build if checksums don't match.
     *
     * @return Option value
     */
    default boolean strictChecksums() {
        return isStrictChecksums();
    }

    /**
     * Fail the build if checksums don't match.
     *
     * @param strictChecksums Option value
     */
    default void strictChecksums(final boolean strictChecksums) {
        setStrictChecksums(strictChecksums);
    }

    /**
     * Warn if checksums don't match.
     *
     * @return Option value
     */
    boolean isLaxChecksums();

    /**
     * Warn if checksums don't match.
     *
     * @param laxChecksums Option value
     */
    void setLaxChecksums(boolean laxChecksums);

    /**
     * Warn if checksums don't match.
     *
     * @return Option value
     */
    default boolean laxChecksums() {
        return isLaxChecksums();
    }

    /**
     * Warn if checksums don't match.
     *
     * @param laxChecksums Option value
     */
    default void laxChecksums(final boolean laxChecksums) {
        setLaxChecksums(laxChecksums);
    }

    /**
     * Defines the color mode of the output. Supported are 'auto', 'always', 'never'.
     *
     * @return Option value
     */
    String getColor();

    /**
     * Defines the color mode of the output. Supported are 'auto', 'always', 'never'.
     *
     * @param color Option value
     */
    void setColor(String color);

    /**
     * Defines the color mode of the output. Supported are 'auto', 'always', 'never'.
     *
     * @return Option value
     */
    default String color() {
        return getColor();
    }

    /**
     * Defines the color mode of the output. Supported are 'auto', 'always', 'never'.
     *
     * @param color Option value
     */
    default void color(final String color) {
        setColor(color);
    }

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @return Option value
     */
    boolean isCheckPluginUpdates();

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @param checkPluginUpdates Option value
     */
    void setCheckPluginUpdates(boolean checkPluginUpdates);

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @return Option value
     */
    default boolean checkPluginUpdates() {
        return isCheckPluginUpdates();
    }

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @param checkPluginUpdates Option value
     */
    default void checkPluginUpdates(final boolean checkPluginUpdates) {
        setCheckPluginUpdates(checkPluginUpdates);
    }

    /**
     * Define a system property.
     *
     * @return Option value
     */
    Map<String, String> getDefine();

    /**
     * Define a system property.
     *
     * @param define Option value
     */
    void setDefine(Map<String, String> define);

    /**
     * Define a system property.
     *
     * @return Option value
     */
    default Map<String, String> define() {
        return getDefine();
    }

    /**
     * Define a system property.
     *
     * @param define Option value
     */
    default void define(final Map<String, String> define) {
        setDefine(define);
    }

    /**
     * Produce execution error messages.
     *
     * @return Option value
     */
    boolean isErrors();

    /**
     * Produce execution error messages.
     *
     * @param errors Option value
     */
    void setErrors(boolean errors);

    /**
     * Produce execution error messages.
     *
     * @return Option value
     */
    default boolean errors() {
        return isErrors();
    }

    /**
     * Produce execution error messages.
     *
     * @param errors Option value
     */
    default void errors(final boolean errors) {
        setErrors(errors);
    }

    /**
     * Encrypt master security password.
     *
     * @return Option value
     */
    String getEncryptMasterPassword();

    /**
     * Encrypt master security password.
     *
     * @param encryptMasterPassword Option value
     */
    void setEncryptMasterPassword(String encryptMasterPassword);

    /**
     * Encrypt master security password.
     *
     * @return Option value
     */
    default String encryptMasterPassword() {
        return getEncryptMasterPassword();
    }

    /**
     * Encrypt master security password.
     *
     * @param encryptMasterPassword Option value
     */
    default void encryptMasterPassword(final String encryptMasterPassword) {
        setEncryptMasterPassword(encryptMasterPassword);
    }

    /**
     * Encrypt server password.
     *
     * @return Option value
     */
    String getEncryptPassword();

    /**
     * Encrypt server password.
     *
     * @param encryptPassword Option value
     */
    void setEncryptPassword(String encryptPassword);

    /**
     * Encrypt server password.
     *
     * @return Option value
     */
    default String encryptPassword() {
        return getEncryptPassword();
    }

    /**
     * Encrypt server password.
     *
     * @param encryptPassword Option value
     */
    default void encryptPassword(final String encryptPassword) {
        setEncryptPassword(encryptPassword);
    }

    /**
     * Force the use of an alternate POM file (or directory with pom.xml).
     *
     * @return Option value
     */
    File getFile();

    /**
     * Force the use of an alternate POM file (or directory with pom.xml).
     *
     * @param file Option value
     */
    void setFile(File file);

    /**
     * Force the use of an alternate POM file (or directory with pom.xml).
     *
     * @return Option value
     */
    default File file() {
        return getFile();
    }

    /**
     * Force the use of an alternate POM file (or directory with pom.xml).
     *
     * @param file Option value
     */
    default void file(final File file) {
        setFile(file);
    }

    /**
     * Only fail the build afterwards; allow all non-impacted builds to continue.
     *
     * @return Option value
     */
    boolean isFailAtEnd();

    /**
     * Only fail the build afterwards; allow all non-impacted builds to continue.
     *
     * @param failAtEnd Option value
     */
    void setFailAtEnd(boolean failAtEnd);

    /**
     * Only fail the build afterwards; allow all non-impacted builds to continue.
     *
     * @return Option value
     */
    default boolean failAtEnd() {
        return isFailAtEnd();
    }

    /**
     * Only fail the build afterwards; allow all non-impacted builds to continue.
     *
     * @param failAtEnd Option value
     */
    default void failAtEnd(final boolean failAtEnd) {
        setFailAtEnd(failAtEnd);
    }

    /**
     * Stop at first failure in reactorized builds.
     *
     * @return Option value
     */
    boolean isFailFast();

    /**
     * Stop at first failure in reactorized builds.
     *
     * @param failFast Option value
     */
    void setFailFast(boolean failFast);

    /**
     * Stop at first failure in reactorized builds.
     *
     * @return Option value
     */
    default boolean failFast() {
        return isFailFast();
    }

    /**
     * Stop at first failure in reactorized builds.
     *
     * @param failFast Option value
     */
    default void failFast(final boolean failFast) {
        setFailFast(failFast);
    }

    /**
     * NEVER fail the build, regardless of project result.
     *
     * @return Option value
     */
    boolean isFailNever();

    /**
     * NEVER fail the build, regardless of project result.
     *
     * @param failNever Option value
     */
    void setFailNever(boolean failNever);

    /**
     * NEVER fail the build, regardless of project result.
     *
     * @return Option value
     */
    default boolean failNever() {
        return isFailNever();
    }

    /**
     * NEVER fail the build, regardless of project result.
     *
     * @param failNever Option value
     */
    default void failNever(final boolean failNever) {
        setFailNever(failNever);
    }

    /**
     * Alternate path for the global settings file.
     *
     * @return Option value
     */
    File getGlobalSettings();

    /**
     * Alternate path for the global settings file.
     *
     * @param globalSettings Option value
     */
    void setGlobalSettings(File globalSettings);

    /**
     * Alternate path for the global settings file.
     *
     * @return Option value
     */
    default File globalSettings() {
        return getGlobalSettings();
    }

    /**
     * Alternate path for the global settings file.
     *
     * @param globalSettings Option value
     */
    default void globalSettings(final File globalSettings) {
        setGlobalSettings(globalSettings);
    }

    /**
     * Alternate path for the global toolchains file.
     *
     * @return Option value
     */
    File getGlobalToolchains();

    /**
     * Alternate path for the global toolchains file.
     *
     * @param globalToolchains Option value
     */
    void setGlobalToolchains(File globalToolchains);

    /**
     * Alternate path for the global toolchains file.
     *
     * @return Option value
     */
    default File globalToolchains() {
        return getGlobalToolchains();
    }

    /**
     * Alternate path for the global toolchains file.
     *
     * @param globalToolchains Option value
     */
    default void globalToolchains(final File globalToolchains) {
        setGlobalToolchains(globalToolchains);
    }

    /**
     * Display help information.
     *
     * @return Option value
     */
    boolean isHelp();

    /**
     * Display help information.
     *
     * @param help Option value
     */
    void setHelp(boolean help);

    /**
     * Display help information.
     *
     * @return Option value
     */
    default boolean help() {
        return isHelp();
    }

    /**
     * Display help information.
     *
     * @param help Option value
     */
    default void help(final boolean help) {
        setHelp(help);
    }

    /**
     * If set, Maven will ignore remote repositories introduced by transitive dependencies.
     *
     * @return Option value
     */
    boolean isIgnoreTransitiveRepositories();

    /**
     * If set, Maven will ignore remote repositories introduced by transitive dependencies.
     *
     * @param ignoreTransitiveRepositories Option value
     */
    void setIgnoreTransitiveRepositories(boolean ignoreTransitiveRepositories);

    /**
     * If set, Maven will ignore remote repositories introduced by transitive dependencies.
     *
     * @return Option value
     */
    default boolean ignoreTransitiveRepositories() {
        return isIgnoreTransitiveRepositories();
    }

    /**
     * If set, Maven will ignore remote repositories introduced by transitive dependencies.
     *
     * @param ignoreTransitiveRepositories Option value
     */
    default void ignoreTransitiveRepositories(final boolean ignoreTransitiveRepositories) {
        setIgnoreTransitiveRepositories(ignoreTransitiveRepositories);
    }

    /**
     * Log file where all build output will go (disables output color).
     *
     * @return Option value
     */
    File getLogFile();

    /**
     * Log file where all build output will go (disables output color).
     *
     * @param logFile Option value
     */
    void setLogFile(File logFile);

    /**
     * Log file where all build output will go (disables output color).
     *
     * @return Option value
     */
    default File logFile() {
        return getLogFile();
    }

    /**
     * Log file where all build output will go (disables output color).
     *
     * @param logFile Option value
     */
    default void logFile(final File logFile) {
        setLogFile(logFile);
    }

    /**
     * Use Maven 2 Legacy Local Repository behaviour, ie no use of _remote.repositories.
     * Can also be activated by using -Dmaven.legacyLocalRepo=true
     *
     * @return Option value
     */
    boolean isLegacyLocalRepository();

    /**
     * Use Maven 2 Legacy Local Repository behaviour, ie no use of _remote.repositories.
     * Can also be activated by using -Dmaven.legacyLocalRepo=true
     *
     * @param legacyLocalRepository Option value
     */
    void setLegacyLocalRepository(boolean legacyLocalRepository);

    /**
     * Use Maven 2 Legacy Local Repository behaviour, ie no use of _remote.repositories.
     * Can also be activated by using -Dmaven.legacyLocalRepo=true
     *
     * @return Option value
     */
    default boolean legacyLocalRepository() {
        return isLegacyLocalRepository();
    }

    /**
     * Use Maven 2 Legacy Local Repository behaviour, ie no use of _remote.repositories.
     * Can also be activated by using -Dmaven.legacyLocalRepo=true
     *
     * @param legacyLocalRepository Option value
     */
    default void legacyLocalRepository(final boolean legacyLocalRepository) {
        setLegacyLocalRepository(legacyLocalRepository);
    }

    /**
     * Do not recurse into sub-projects.
     *
     * @return Option value
     */
    boolean isNonRecursive();

    /**
     * Do not recurse into sub-projects.
     *
     * @param nonRecursive Option value
     */
    void setNonRecursive(boolean nonRecursive);

    /**
     * Do not recurse into sub-projects.
     *
     * @return Option value
     */
    default boolean nonRecursive() {
        return isNonRecursive();
    }

    /**
     * Do not recurse into sub-projects.
     *
     * @param nonRecursive Option value
     */
    default void nonRecursive(final boolean nonRecursive) {
        setNonRecursive(nonRecursive);
    }

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @return Option value
     */
    boolean isNoPluginRegistry();

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @param noPluginRegistry Option value
     */
    void setNoPluginRegistry(boolean noPluginRegistry);

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @return Option value
     */
    default boolean noPluginRegistry() {
        return isNoPluginRegistry();
    }

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @param noPluginRegistry Option value
     */
    default void noPluginRegistry(final boolean noPluginRegistry) {
        setNoPluginRegistry(noPluginRegistry);
    }

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @return Option value
     */
    boolean isNoPluginUpdates();

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @param noPluginUpdates Option value
     */
    void setNoPluginUpdates(boolean noPluginUpdates);

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @return Option value
     */
    default boolean noPluginUpdates() {
        return isNoPluginUpdates();
    }

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @param noPluginUpdates Option value
     */
    default void noPluginUpdates(final boolean noPluginUpdates) {
        setNoPluginUpdates(noPluginUpdates);
    }

    /**
     * Suppress SNAPSHOT updates.
     *
     * @return Option value
     */
    boolean isNoSnapshotUpdates();

    /**
     * Suppress SNAPSHOT updates.
     *
     * @param noSnapshotUpdates Option value
     */
    void setNoSnapshotUpdates(boolean noSnapshotUpdates);

    /**
     * Suppress SNAPSHOT updates.
     *
     * @return Option value
     */
    default boolean noSnapshotUpdates() {
        return isNoSnapshotUpdates();
    }

    /**
     * Suppress SNAPSHOT updates.
     *
     * @param noSnapshotUpdates Option value
     */
    default void noSnapshotUpdates(final boolean noSnapshotUpdates) {
        setNoSnapshotUpdates(noSnapshotUpdates);
    }

    /**
     * Do not display transfer progress when downloading or uploading.
     *
     * @return Option value
     */
    boolean isNoTransferProgress();

    /**
     * Do not display transfer progress when downloading or uploading.
     *
     * @param noTransferProgress Option value
     */
    void setNoTransferProgress(boolean noTransferProgress);

    /**
     * Do not display transfer progress when downloading or uploading.
     *
     * @return Option value
     */
    default boolean noTransferProgress() {
        return isNoTransferProgress();
    }

    /**
     * Do not display transfer progress when downloading or uploading.
     *
     * @param noTransferProgress Option value
     */
    default void noTransferProgress(final boolean noTransferProgress) {
        setNoTransferProgress(noTransferProgress);
    }

    /**
     * Work offline.
     *
     * @return Option value
     */
    boolean isOffline();

    /**
     * Work offline.
     *
     * @param offline Option value
     */
    void setOffline(boolean offline);

    /**
     * Work offline.
     *
     * @return Option value
     */
    default boolean offline() {
        return isOffline();
    }

    /**
     * Work offline.
     *
     * @param offline Option value
     */
    default void offline(final boolean offline) {
        setOffline(offline);
    }

    /**
     * Comma-delimited list of profiles to activate.
     *
     * @return Option value
     */
    String[] getActivateProfiles();

    /**
     * Comma-delimited list of profiles to activate.
     *
     * @param activateProfiles Option value
     */
    @SuppressWarnings("PMD.UseVarargs")
    void setActivateProfiles(String[] activateProfiles);

    /**
     * Comma-delimited list of profiles to activate.
     *
     * @return Option value
     */
    default String[] activateProfiles() {
        return getActivateProfiles();
    }

    /**
     * Comma-delimited list of profiles to activate.
     *
     * @param activateProfiles Option value
     */
    @SuppressWarnings("PMD.UseVarargs")
    default void activateProfiles(final String[] activateProfiles) {
        setActivateProfiles(activateProfiles);
    }

    /**
     * Comma-delimited list of specified reactor projects to build instead of all projects.
     * A project can be specified by [groupId]:artifactId or by its relative path
     *
     * @return Option value
     */
    String[] getProjects();

    /**
     * Comma-delimited list of specified reactor projects to build instead of all projects.
     * A project can be specified by [groupId]:artifactId or by its relative path
     *
     * @param projects Option value
     */
    @SuppressWarnings("PMD.UseVarargs")
    void setProjects(String[] projects);

    /**
     * Comma-delimited list of specified reactor projects to build instead of all projects.
     * A project can be specified by [groupId]:artifactId or by its relative path
     *
     * @return Option value
     */
    default String[] projects() {
        return getProjects();
    }

    /**
     * Comma-delimited list of specified reactor projects to build instead of all projects.
     * A project can be specified by [groupId]:artifactId or by its relative path
     *
     * @param projects Option value
     */
    @SuppressWarnings("PMD.UseVarargs")
    default void projects(final String[] projects) {
        setProjects(projects);
    }

    /**
     * Quiet output - only show errors.
     *
     * @return Option value
     */
    boolean isQuiet();

    /**
     * Quiet output - only show errors.
     *
     * @param quiet Option value
     */
    void setQuiet(boolean quiet);

    /**
     * Quiet output - only show errors.
     *
     * @return Option value
     */
    default boolean quiet() {
        return isQuiet();
    }

    /**
     * Quiet output - only show errors.
     *
     * @param quiet Option value
     */
    default void quiet(final boolean quiet) {
        setQuiet(quiet);
    }

    /**
     * Resume reactor from specified project.
     *
     * @return Option value
     */
    String getResumeFrom();

    /**
     * Resume reactor from specified project.
     *
     * @param resumeFrom Option value
     */
    void setResumeFrom(String resumeFrom);

    /**
     * Resume reactor from specified project.
     *
     * @return Option value
     */
    default String resumeFrom() {
        return getResumeFrom();
    }

    /**
     * Resume reactor from specified project.
     *
     * @param resumeFrom Option value
     */
    default void resumeFrom(final String resumeFrom) {
        setResumeFrom(resumeFrom);
    }

    /**
     * Alternate path for the user settings file.
     *
     * @return Option value
     */
    File getSettings();

    /**
     * Alternate path for the user settings file.
     *
     * @param settings Option value
     */
    void setSettings(File settings);

    /**
     * Alternate path for the user settings file.
     *
     * @return Option value
     */
    default File settings() {
        return getSettings();
    }

    /**
     * Alternate path for the user settings file.
     *
     * @param settings Option value
     */
    default void settings(final File settings) {
        setSettings(settings);
    }

    /**
     * Alternate path for the user toolchains file.
     *
     * @return Option value
     */
    File getToolchains();

    /**
     * Alternate path for the user toolchains file.
     *
     * @param toolchains Option value
     */
    void setToolchains(File toolchains);

    /**
     * Alternate path for the user toolchains file.
     *
     * @return Option value
     */
    default File toolchains() {
        return getToolchains();
    }

    /**
     * Alternate path for the user toolchains file.
     *
     * @param toolchains Option value
     */
    default void toolchains(final File toolchains) {
        setToolchains(toolchains);
    }

    /**
     * Thread count, for instance 2.0C where C is core multiplied.
     *
     * @return Option value
     */
    String getThreads();

    /**
     * Thread count, for instance 2.0C where C is core multiplied.
     *
     * @param threads Option value
     */
    void setThreads(String threads);

    /**
     * Thread count, for instance 2.0C where C is core multiplied.
     *
     * @return Option value
     */
    default String threads() {
        return getThreads();
    }

    /**
     * Thread count, for instance 2.0C where C is core multiplied.
     *
     * @param threads Option value
     */
    default void threads(final String threads) {
        setThreads(threads);
    }

    /**
     * Forces a check for missing releases and updated snapshots on remote repositories.
     *
     * @return Option value
     */
    boolean isUpdateSnapshots();

    /**
     * Forces a check for missing releases and updated snapshots on remote repositories.
     *
     * @param updateSnapshots Option value
     */
    void setUpdateSnapshots(boolean updateSnapshots);

    /**
     * Forces a check for missing releases and updated snapshots on remote repositories.
     *
     * @return Option value
     */
    default boolean updateSnapshots() {
        return isUpdateSnapshots();
    }

    /**
     * Forces a check for missing releases and updated snapshots on remote repositories.
     *
     * @param updateSnapshots Option value
     */
    default void updateSnapshots(final boolean updateSnapshots) {
        setUpdateSnapshots(updateSnapshots);
    }

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @return Option value
     */
    boolean isUpdatePlugins();

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @param updatePlugins Option value
     */
    void setUpdatePlugins(boolean updatePlugins);

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @return Option value
     */
    default boolean updatePlugins() {
        return isUpdatePlugins();
    }

    /**
     * Ineffective, only kept for backward compatibility.
     *
     * @param updatePlugins Option value
     */
    default void updatePlugins(final boolean updatePlugins) {
        setUpdatePlugins(updatePlugins);
    }

    /**
     * Display version information.
     *
     * @return Option value
     */
    boolean isVersion();

    /**
     * Display version information.
     *
     * @param version Option value
     */
    void setVersion(boolean version);

    /**
     * Display version information.
     *
     * @return Option value
     */
    default boolean version() {
        return isVersion();
    }

    /**
     * Display version information.
     *
     * @param version Option value
     */
    default void version(final boolean version) {
        setVersion(version);
    }

    /**
     * Display version information WITHOUT stopping build.
     *
     * @return Option value
     */
    boolean isShowVersion();

    /**
     * Display version information WITHOUT stopping build.
     *
     * @param showVersion Option value
     */
    void setShowVersion(boolean showVersion);

    /**
     * Display version information WITHOUT stopping build.
     *
     * @return Option value
     */
    default boolean showVersion() {
        return isShowVersion();
    }

    /**
     * Display version information WITHOUT stopping build.
     *
     * @param showVersion Option value
     */
    default void showVersion(final boolean showVersion) {
        setShowVersion(showVersion);
    }

    /**
     * Produce execution debug output.
     *
     * @return Option value
     */
    boolean isDebug();

    /**
     * Produce execution debug output.
     *
     * @param debug Option value
     */
    void setDebug(boolean debug);

    /**
     * Produce execution debug output.
     *
     * @return Option value
     */
    default boolean debug() {
        return isDebug();
    }

    /**
     * Produce execution debug output.
     *
     * @param debug Option value
     */
    default void debug(final boolean debug) {
        setDebug(debug);
    }
}
