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
import java.util.*;

/**
 * Default implementation of configuration options for any supported version of Maven.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@SuppressWarnings({
        "PMD.ExcessivePublicCount",
        "PMD.CyclomaticComplexity",
        "PMD.TooManyFields",
        "PMD.GodClass"
})
public class DefaultMavenOptions implements MavenOptions, Serializable {
    private static final long serialVersionUID = 5202374569318582383L;

    private boolean alsoMake;
    private boolean alsoMakeDependents;
    private boolean batchMode;
    private String builder;
    private boolean strictChecksums;
    private boolean laxChecksums;
    private String color;
    private boolean checkPluginUpdates;
    private Map<String, String> define;
    private boolean errors;
    private String encryptMasterPassword;
    private String encryptPassword;
    private File file;
    private boolean failAtEnd;
    private boolean failFast;
    private boolean failNever;
    private File globalSettings;
    private File globalToolchains;
    private boolean help;
    private boolean ignoreTransitiveRepositories;
    private File logFile;
    private boolean legacyLocalRepository;
    private boolean nonRecursive;
    private boolean noPluginRegistry;
    private boolean noPluginUpdates;
    private boolean noSnapshotUpdates;
    private boolean noTransferProgress;
    private boolean offline;
    private String[] activateProfiles;
    private String[] projects;
    private boolean quiet;
    private String resumeFrom;
    private File settings;
    private File toolchains;
    private String threads;
    private boolean updateSnapshots;
    private boolean updatePlugins;
    private boolean version;
    private boolean showVersion;
    private boolean debug;

    @Override
    public boolean isAlsoMake() {
        return alsoMake;
    }

    @Override
    public void setAlsoMake(final boolean alsoMake) {
        this.alsoMake = alsoMake;
    }

    @Override
    public boolean isAlsoMakeDependents() {
        return alsoMakeDependents;
    }

    @Override
    public void setAlsoMakeDependents(final boolean alsoMakeDependents) {
        this.alsoMakeDependents = alsoMakeDependents;
    }

    @Override
    public boolean isBatchMode() {
        return batchMode;
    }

    @Override
    public void setBatchMode(final boolean batchMode) {
        this.batchMode = batchMode;
    }

    @Override
    public String getBuilder() {
        return builder;
    }

    @Override
    public void setBuilder(final String builder) {
        this.builder = builder;
    }

    @Override
    public boolean isStrictChecksums() {
        return strictChecksums;
    }

    @Override
    public void setStrictChecksums(final boolean strictChecksums) {
        this.strictChecksums = strictChecksums;
    }

    @Override
    public boolean isLaxChecksums() {
        return laxChecksums;
    }

    @Override
    public void setLaxChecksums(final boolean laxChecksums) {
        this.laxChecksums = laxChecksums;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setColor(final String color) {
        this.color = color;
    }

    @Override
    public boolean isCheckPluginUpdates() {
        return checkPluginUpdates;
    }

    @Override
    public void setCheckPluginUpdates(final boolean checkPluginUpdates) {
        this.checkPluginUpdates = checkPluginUpdates;
    }

    @Override
    public Map<String, String> getDefine() {
        return define;
    }

    @Override
    public void setDefine(final Map<String, String> define) {
        this.define = define;
    }

    @Override
    public boolean isErrors() {
        return errors;
    }

    @Override
    public void setErrors(final boolean errors) {
        this.errors = errors;
    }

    @Override
    public String getEncryptMasterPassword() {
        return encryptMasterPassword;
    }

    @Override
    public void setEncryptMasterPassword(final String encryptMasterPassword) {
        this.encryptMasterPassword = encryptMasterPassword;
    }

    @Override
    public String getEncryptPassword() {
        return encryptPassword;
    }

    @Override
    public void setEncryptPassword(final String encryptPassword) {
        this.encryptPassword = encryptPassword;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setFile(final File file) {
        this.file = file;
    }

    @Override
    public boolean isFailAtEnd() {
        return failAtEnd;
    }

    @Override
    public void setFailAtEnd(final boolean failAtEnd) {
        this.failAtEnd = failAtEnd;
    }

    @Override
    public boolean isFailFast() {
        return failFast;
    }

    @Override
    public void setFailFast(final boolean failFast) {
        this.failFast = failFast;
    }

    @Override
    public boolean isFailNever() {
        return failNever;
    }

    @Override
    public void setFailNever(final boolean failNever) {
        this.failNever = failNever;
    }

    @Override
    public File getGlobalSettings() {
        return globalSettings;
    }

    @Override
    public void setGlobalSettings(final File globalSettings) {
        this.globalSettings = globalSettings;
    }

    @Override
    public File getGlobalToolchains() {
        return globalToolchains;
    }

    @Override
    public void setGlobalToolchains(final File globalToolchains) {
        this.globalToolchains = globalToolchains;
    }

    @Override
    public boolean isHelp() {
        return help;
    }

    @Override
    public void setHelp(final boolean help) {
        this.help = help;
    }

    @Override
    public boolean isIgnoreTransitiveRepositories() {
        return ignoreTransitiveRepositories;
    }

    @Override
    public void setIgnoreTransitiveRepositories(final boolean ignoreTransitiveRepositories) {
        this.ignoreTransitiveRepositories = ignoreTransitiveRepositories;
    }

    @Override
    public File getLogFile() {
        return logFile;
    }

    @Override
    public void setLogFile(final File logFile) {
        this.logFile = logFile;
    }

    @Override
    public boolean isLegacyLocalRepository() {
        return legacyLocalRepository;
    }

    @Override
    public void setLegacyLocalRepository(final boolean legacyLocalRepository) {
        this.legacyLocalRepository = legacyLocalRepository;
    }

    @Override
    public boolean isNonRecursive() {
        return nonRecursive;
    }

    @Override
    public void setNonRecursive(final boolean nonRecursive) {
        this.nonRecursive = nonRecursive;
    }

    @Override
    public boolean isNoPluginRegistry() {
        return noPluginRegistry;
    }

    @Override
    public void setNoPluginRegistry(final boolean noPluginRegistry) {
        this.noPluginRegistry = noPluginRegistry;
    }

    @Override
    public boolean isNoPluginUpdates() {
        return noPluginUpdates;
    }

    @Override
    public void setNoPluginUpdates(final boolean noPluginUpdates) {
        this.noPluginUpdates = noPluginUpdates;
    }

    @Override
    public boolean isNoSnapshotUpdates() {
        return noSnapshotUpdates;
    }

    @Override
    public void setNoSnapshotUpdates(final boolean noSnapshotUpdates) {
        this.noSnapshotUpdates = noSnapshotUpdates;
    }

    @Override
    public boolean isNoTransferProgress() {
        return noTransferProgress;
    }

    @Override
    public void setNoTransferProgress(final boolean noTransferProgress) {
        this.noTransferProgress = noTransferProgress;
    }

    @Override
    public boolean isOffline() {
        return offline;
    }

    @Override
    public void setOffline(final boolean offline) {
        this.offline = offline;
    }

    @Override
    public String[] getActivateProfiles() {
        return copyStringArray(activateProfiles);
    }

    @Override
    public void setActivateProfiles(final String[] activateProfiles) {
        this.activateProfiles = activateProfiles;
    }

    @Override
    public String[] getProjects() {
        return copyStringArray(projects);
    }

    @Override
    public void setProjects(final String[] projects) {
        this.projects = projects;
    }

    @Override
    public boolean isQuiet() {
        return quiet;
    }

    @Override
    public void setQuiet(final boolean quiet) {
        this.quiet = quiet;
    }

    @Override
    public String getResumeFrom() {
        return resumeFrom;
    }

    @Override
    public void setResumeFrom(final String resumeFrom) {
        this.resumeFrom = resumeFrom;
    }

    @Override
    public File getSettings() {
        return settings;
    }

    @Override
    public void setSettings(final File settings) {
        this.settings = settings;
    }

    @Override
    public File getToolchains() {
        return toolchains;
    }

    @Override
    public void setToolchains(final File toolchains) {
        this.toolchains = toolchains;
    }

    @Override
    public String getThreads() {
        return threads;
    }

    @Override
    public void setThreads(final String threads) {
        this.threads = threads;
    }

    @Override
    public boolean isUpdateSnapshots() {
        return updateSnapshots;
    }

    @Override
    public void setUpdateSnapshots(final boolean updateSnapshots) {
        this.updateSnapshots = updateSnapshots;
    }

    @Override
    public boolean isUpdatePlugins() {
        return updatePlugins;
    }

    @Override
    public void setUpdatePlugins(final boolean updatePlugins) {
        this.updatePlugins = updatePlugins;
    }

    @Override
    public boolean isVersion() {
        return version;
    }

    @Override
    public void setVersion(final boolean version) {
        this.version = version;
    }

    @Override
    public boolean isShowVersion() {
        return showVersion;
    }

    @Override
    public void setShowVersion(final boolean showVersion) {
        this.showVersion = showVersion;
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

    @Override
    @SuppressWarnings({
            "checkstyle:CyclomaticComplexity",
            "checkstyle:NPathComplexity",
            "PMD.NPathComplexity"
    })
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DefaultMavenOptions that = (DefaultMavenOptions) o;
        return alsoMake == that.alsoMake
                && alsoMakeDependents == that.alsoMakeDependents
                && batchMode == that.batchMode
                && strictChecksums == that.strictChecksums
                && laxChecksums == that.laxChecksums
                && checkPluginUpdates == that.checkPluginUpdates
                && errors == that.errors
                && failAtEnd == that.failAtEnd
                && failFast == that.failFast
                && failNever == that.failNever
                && help == that.help
                && ignoreTransitiveRepositories == that.ignoreTransitiveRepositories
                && legacyLocalRepository == that.legacyLocalRepository
                && nonRecursive == that.nonRecursive
                && noPluginRegistry == that.noPluginRegistry
                && noPluginUpdates == that.noPluginUpdates
                && noSnapshotUpdates == that.noSnapshotUpdates
                && noTransferProgress == that.noTransferProgress
                && offline == that.offline
                && quiet == that.quiet
                && updateSnapshots == that.updateSnapshots
                && updatePlugins == that.updatePlugins
                && version == that.version
                && showVersion == that.showVersion
                && debug == that.debug
                && Objects.equals(builder, that.builder)
                && Objects.equals(color, that.color)
                && Objects.equals(define, that.define)
                && Objects.equals(encryptMasterPassword, that.encryptMasterPassword)
                && Objects.equals(encryptPassword, that.encryptPassword)
                && Objects.equals(file, that.file)
                && Objects.equals(globalSettings, that.globalSettings)
                && Objects.equals(globalToolchains, that.globalToolchains)
                && Objects.equals(logFile, that.logFile)
                && Arrays.equals(activateProfiles, that.activateProfiles)
                && Arrays.equals(projects, that.projects)
                && Objects.equals(resumeFrom, that.resumeFrom)
                && Objects.equals(settings, that.settings)
                && Objects.equals(toolchains, that.toolchains)
                && Objects.equals(threads, that.threads);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(alsoMake, alsoMakeDependents, batchMode, builder, strictChecksums, laxChecksums,
                color, checkPluginUpdates, define, errors, encryptMasterPassword, encryptPassword, file, failAtEnd,
                failFast, failNever, globalSettings, globalToolchains, help, ignoreTransitiveRepositories, logFile,
                legacyLocalRepository, nonRecursive, noPluginRegistry, noPluginUpdates, noSnapshotUpdates,
                noTransferProgress, offline, quiet, resumeFrom, settings, toolchains, threads, updateSnapshots,
                updatePlugins, version, showVersion, debug);
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
                .add("ignoreTransitiveRepositories=" + ignoreTransitiveRepositories)
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

    @SuppressWarnings("PMD.UseVarargs")
    private String[] copyStringArray(final String[] array) {
        return Optional.ofNullable(array)
                .filter(strings -> strings.length > 0)
                .map(strings -> Arrays.copyOf(strings, strings.length))
                .orElse(null);
    }
}
