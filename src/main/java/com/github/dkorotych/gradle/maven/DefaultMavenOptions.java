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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class DefaultMavenOptions implements MavenOptions, Serializable {
    /**
     * If project list is specified, also build projects required by the list
     */
    private boolean alsoMake;
    /**
     * If project list is specified, also build projects that depend on projects on the list
     */
    private boolean alsoMakeDependents;
    /**
     * Run in non-interactive (batch) mode (disables output color)
     */
    private boolean batchMode;
    /**
     * The id of the build strategy to use
     */
    private String builder;
    /**
     * Fail the build if checksums don't match
     */
    private boolean strictChecksums;
    /**
     * Warn if checksums don't match
     */
    private boolean laxChecksums;
    /**
     * Defines the color mode of the output. Supported are 'auto', 'always', 'never'.
     */
    private String color;
    /**
     * Ineffective, only kept for backward compatibility
     */
    private boolean checkPluginUpdates;
    /**
     * Define a system property
     */
    private Map<String, String> define;
    /**
     * Produce execution error messages
     */
    private boolean errors;
    /**
     * Encrypt master security password
     */
    private String encryptMasterPassword;
    /**
     * Encrypt server password
     */
    private String encryptPassword;
    /**
     * Force the use of an alternate POM file (or directory with pom.xml)
     */
    private File file;
    /**
     * Only fail the build afterwards; allow all non-impacted builds to continue
     */
    private boolean failAtEnd;
    /**
     * Stop at first failure in reactorized builds
     */
    private boolean failFast;
    /**
     * NEVER fail the build, regardless of project result
     */
    private boolean failNever;
    /**
     * Alternate path for the global settings file
     */
    private File globalSettings;
    /**
     * Alternate path for the global toolchains file
     */
    private File globalToolchains;
    /**
     * Display help information
     */
    private boolean help;
    /**
     * Log file where all build output will go (disables output color)
     */
    private File logFile;
    /**
     * Use Maven 2 Legacy Local Repository behaviour, ie no use of _remote.repositories.
     * Can also be activated by using -Dmaven.legacyLocalRepo=true
     */
    private boolean legacyLocalRepository;
    /**
     * Do not recurse into sub-projects
     */
    private boolean nonRecursive;
    /**
     * Ineffective, only kept for backward compatibility
     */
    private boolean noPluginRegistry;
    /**
     * Ineffective, only kept for backward compatibility
     */
    private boolean noPluginUpdates;
    /**
     * Suppress SNAPSHOT updates
     */
    private boolean noSnapshotUpdates;
    /**
     * Do not display transfer progress when downloading or uploading
     */
    private boolean noTransferProgress;
    /**
     * Work offline
     */
    private boolean offline;
    /**
     * Comma-delimited list of profiles to activate
     */
    private String[] activateProfiles;
    /**
     * Comma-delimited list of specified reactor projects to build instead of all projects.
     * A project can be specified by [groupId]:artifactId or by its relative path
     */
    private String[] projects;
    /**
     * Quiet output - only show errors
     */
    private boolean quiet;
    /**
     * Resume reactor from specified project
     */
    private String resumeFrom;
    /**
     * Alternate path for the user settings file
     */
    private File settings;
    /**
     * Alternate path for the user toolchains file
     */
    private File toolchains;
    /**
     * Thread count, for instance 2.0C where C is core multiplied
     */
    private String threads;
    /**
     * Forces a check for missing releases and updated snapshots on remote repositories
     */
    private boolean updateSnapshots;
    /**
     * Ineffective, only kept for backward compatibility
     */
    private boolean updatePlugins;
    /**
     * Display version information
     */
    private boolean version;
    /**
     * Display version information WITHOUT stopping build
     */
    private boolean showVersion;
    /**
     * Produce execution debug output
     */
    private boolean debug;

    @Override
    public boolean isAlsoMake() {
        return alsoMake;
    }

    @Override
    public void setAlsoMake(boolean alsoMake) {
        this.alsoMake = alsoMake;
    }

    @Override
    public boolean isAlsoMakeDependents() {
        return alsoMakeDependents;
    }

    @Override
    public void setAlsoMakeDependents(boolean alsoMakeDependents) {
        this.alsoMakeDependents = alsoMakeDependents;
    }

    @Override
    public boolean isBatchMode() {
        return batchMode;
    }

    @Override
    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

    @Override
    public String getBuilder() {
        return builder;
    }

    @Override
    public void setBuilder(String builder) {
        this.builder = builder;
    }

    @Override
    public boolean isStrictChecksums() {
        return strictChecksums;
    }

    @Override
    public void setStrictChecksums(boolean strictChecksums) {
        this.strictChecksums = strictChecksums;
    }

    @Override
    public boolean isLaxChecksums() {
        return laxChecksums;
    }

    @Override
    public void setLaxChecksums(boolean laxChecksums) {
        this.laxChecksums = laxChecksums;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean isCheckPluginUpdates() {
        return checkPluginUpdates;
    }

    @Override
    public void setCheckPluginUpdates(boolean checkPluginUpdates) {
        this.checkPluginUpdates = checkPluginUpdates;
    }

    @Override
    public Map<String, String> getDefine() {
        return define;
    }

    @Override
    public void setDefine(Map<String, String> define) {
        this.define = define;
    }

    @Override
    public boolean isErrors() {
        return errors;
    }

    @Override
    public void setErrors(boolean errors) {
        this.errors = errors;
    }

    @Override
    public String getEncryptMasterPassword() {
        return encryptMasterPassword;
    }

    @Override
    public void setEncryptMasterPassword(String encryptMasterPassword) {
        this.encryptMasterPassword = encryptMasterPassword;
    }

    @Override
    public String getEncryptPassword() {
        return encryptPassword;
    }

    @Override
    public void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public boolean isFailAtEnd() {
        return failAtEnd;
    }

    @Override
    public void setFailAtEnd(boolean failAtEnd) {
        this.failAtEnd = failAtEnd;
    }

    @Override
    public boolean isFailFast() {
        return failFast;
    }

    @Override
    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }

    @Override
    public boolean isFailNever() {
        return failNever;
    }

    @Override
    public void setFailNever(boolean failNever) {
        this.failNever = failNever;
    }

    @Override
    public File getGlobalSettings() {
        return globalSettings;
    }

    @Override
    public void setGlobalSettings(File globalSettings) {
        this.globalSettings = globalSettings;
    }

    @Override
    public File getGlobalToolchains() {
        return globalToolchains;
    }

    @Override
    public void setGlobalToolchains(File globalToolchains) {
        this.globalToolchains = globalToolchains;
    }

    @Override
    public boolean isHelp() {
        return help;
    }

    @Override
    public void setHelp(boolean help) {
        this.help = help;
    }

    @Override
    public File getLogFile() {
        return logFile;
    }

    @Override
    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    @Override
    public boolean isLegacyLocalRepository() {
        return legacyLocalRepository;
    }

    @Override
    public void setLegacyLocalRepository(boolean legacyLocalRepository) {
        this.legacyLocalRepository = legacyLocalRepository;
    }

    @Override
    public boolean isNonRecursive() {
        return nonRecursive;
    }

    @Override
    public void setNonRecursive(boolean nonRecursive) {
        this.nonRecursive = nonRecursive;
    }

    @Override
    public boolean isNoPluginRegistry() {
        return noPluginRegistry;
    }

    @Override
    public void setNoPluginRegistry(boolean noPluginRegistry) {
        this.noPluginRegistry = noPluginRegistry;
    }

    @Override
    public boolean isNoPluginUpdates() {
        return noPluginUpdates;
    }

    @Override
    public void setNoPluginUpdates(boolean noPluginUpdates) {
        this.noPluginUpdates = noPluginUpdates;
    }

    @Override
    public boolean isNoSnapshotUpdates() {
        return noSnapshotUpdates;
    }

    @Override
    public void setNoSnapshotUpdates(boolean noSnapshotUpdates) {
        this.noSnapshotUpdates = noSnapshotUpdates;
    }

    @Override
    public boolean isNoTransferProgress() {
        return noTransferProgress;
    }

    @Override
    public void setNoTransferProgress(boolean noTransferProgress) {
        this.noTransferProgress = noTransferProgress;
    }

    @Override
    public boolean isOffline() {
        return offline;
    }

    @Override
    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    @Override
    public String[] getActivateProfiles() {
        return activateProfiles;
    }

    @Override
    public void setActivateProfiles(String[] activateProfiles) {
        this.activateProfiles = activateProfiles;
    }

    @Override
    public String[] getProjects() {
        return projects;
    }

    @Override
    public void setProjects(String[] projects) {
        this.projects = projects;
    }

    @Override
    public boolean isQuiet() {
        return quiet;
    }

    @Override
    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    @Override
    public String getResumeFrom() {
        return resumeFrom;
    }

    @Override
    public void setResumeFrom(String resumeFrom) {
        this.resumeFrom = resumeFrom;
    }

    @Override
    public File getSettings() {
        return settings;
    }

    @Override
    public void setSettings(File settings) {
        this.settings = settings;
    }

    @Override
    public File getToolchains() {
        return toolchains;
    }

    @Override
    public void setToolchains(File toolchains) {
        this.toolchains = toolchains;
    }

    @Override
    public String getThreads() {
        return threads;
    }

    @Override
    public void setThreads(String threads) {
        this.threads = threads;
    }

    @Override
    public boolean isUpdateSnapshots() {
        return updateSnapshots;
    }

    @Override
    public void setUpdateSnapshots(boolean updateSnapshots) {
        this.updateSnapshots = updateSnapshots;
    }

    @Override
    public boolean isUpdatePlugins() {
        return updatePlugins;
    }

    @Override
    public void setUpdatePlugins(boolean updatePlugins) {
        this.updatePlugins = updatePlugins;
    }

    @Override
    public boolean isVersion() {
        return version;
    }

    @Override
    public void setVersion(boolean version) {
        this.version = version;
    }

    @Override
    public boolean isShowVersion() {
        return showVersion;
    }

    @Override
    public void setShowVersion(boolean showVersion) {
        this.showVersion = showVersion;
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultMavenOptions that = (DefaultMavenOptions) o;
        return alsoMake == that.alsoMake && alsoMakeDependents == that.alsoMakeDependents && batchMode == that.batchMode && strictChecksums == that.strictChecksums && laxChecksums == that.laxChecksums && checkPluginUpdates == that.checkPluginUpdates && errors == that.errors && failAtEnd == that.failAtEnd && failFast == that.failFast && failNever == that.failNever && help == that.help && legacyLocalRepository == that.legacyLocalRepository && nonRecursive == that.nonRecursive && noPluginRegistry == that.noPluginRegistry && noPluginUpdates == that.noPluginUpdates && noSnapshotUpdates == that.noSnapshotUpdates && noTransferProgress == that.noTransferProgress && offline == that.offline && quiet == that.quiet && updateSnapshots == that.updateSnapshots && updatePlugins == that.updatePlugins && version == that.version && showVersion == that.showVersion && debug == that.debug && Objects.equals(builder, that.builder) && Objects.equals(color, that.color) && Objects.equals(define, that.define) && Objects.equals(encryptMasterPassword, that.encryptMasterPassword) && Objects.equals(encryptPassword, that.encryptPassword) && Objects.equals(file, that.file) && Objects.equals(globalSettings, that.globalSettings) && Objects.equals(globalToolchains, that.globalToolchains) && Objects.equals(logFile, that.logFile) && Arrays.equals(activateProfiles, that.activateProfiles) && Arrays.equals(projects, that.projects) && Objects.equals(resumeFrom, that.resumeFrom) && Objects.equals(settings, that.settings) && Objects.equals(toolchains, that.toolchains) && Objects.equals(threads, that.threads);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(alsoMake, alsoMakeDependents, batchMode, builder, strictChecksums, laxChecksums, color, checkPluginUpdates, define, errors, encryptMasterPassword, encryptPassword, file, failAtEnd, failFast, failNever, globalSettings, globalToolchains, help, logFile, legacyLocalRepository, nonRecursive, noPluginRegistry, noPluginUpdates, noSnapshotUpdates, noTransferProgress, offline, quiet, resumeFrom, settings, toolchains, threads, updateSnapshots, updatePlugins, version, showVersion, debug);
        result = 31 * result + Arrays.hashCode(activateProfiles);
        result = 31 * result + Arrays.hashCode(projects);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DefaultMavenOptions.class.getSimpleName() + "[", "]")
                .add("alsoMake=" + alsoMake)
                .add("alsoMakeDependents=" + alsoMakeDependents)
                .add("batchMode=" + batchMode)
                .add("builder='" + builder + "'")
                .add("strictChecksums=" + strictChecksums)
                .add("laxChecksums=" + laxChecksums)
                .add("color='" + color + "'")
                .add("checkPluginUpdates=" + checkPluginUpdates)
                .add("define=" + define)
                .add("errors=" + errors)
                .add("encryptMasterPassword='" + encryptMasterPassword + "'")
                .add("encryptPassword='" + encryptPassword + "'")
                .add("file=" + file)
                .add("failAtEnd=" + failAtEnd)
                .add("failFast=" + failFast)
                .add("failNever=" + failNever)
                .add("globalSettings=" + globalSettings)
                .add("globalToolchains=" + globalToolchains)
                .add("help=" + help)
                .add("logFile=" + logFile)
                .add("legacyLocalRepository=" + legacyLocalRepository)
                .add("nonRecursive=" + nonRecursive)
                .add("noPluginRegistry=" + noPluginRegistry)
                .add("noPluginUpdates=" + noPluginUpdates)
                .add("noSnapshotUpdates=" + noSnapshotUpdates)
                .add("noTransferProgress=" + noTransferProgress)
                .add("offline=" + offline)
                .add("activateProfiles=" + Arrays.toString(activateProfiles))
                .add("projects=" + Arrays.toString(projects))
                .add("quiet=" + quiet)
                .add("resumeFrom='" + resumeFrom + "'")
                .add("settings=" + settings)
                .add("toolchains=" + toolchains)
                .add("threads='" + threads + "'")
                .add("updateSnapshots=" + updateSnapshots)
                .add("updatePlugins=" + updatePlugins)
                .add("version=" + version)
                .add("showVersion=" + showVersion)
                .add("debug=" + debug)
                .toString();
    }
}
