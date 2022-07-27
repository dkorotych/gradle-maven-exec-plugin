/**
 * Copyright 2022 Dmitry Korotych
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
package com.github.dkorotych.gradle.maven.exec;

import com.github.dkorotych.gradle.maven.MavenOptions;
import groovy.lang.Closure;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;
import org.gradle.process.BaseExecSpec;
import org.gradle.util.ClosureBackedAction;
import org.gradle.util.CollectionUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Specifies options for launching a Maven process.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
public interface MavenExecSpec extends BaseExecSpec, MavenOptions {
    /**
     * Returns the Maven directory for the process.
     *
     * @return The Maven directory
     */
    @InputFile
    @Optional
    File getMavenDir();

    /**
     * Sets the Maven directory for the process. The supplied argument is evaluated as per
     * {@link org.gradle.api.Project#file(Object)}.
     *
     * @param dir The Maven directory
     */
    void setMavenDir(File dir);

    /**
     * Sets the Maven directory for the process. The supplied argument is evaluated as per
     * {@link org.gradle.api.Project#file(Object)}.
     *
     * @param dir The Maven directory
     * @return this
     */
    default MavenExecSpec mavenDir(File dir) {
        setMavenDir(dir);
        return this;
    }

    /**
     * Adds goals for the Maven.
     *
     * @param goals goals for the Maven
     * @return this
     */
    default MavenExecSpec goals(String... goals) {
        if (goals != null) {
            return goals(Arrays.asList(goals));
        }
        return this;
    }

    /**
     * Adds goals for the Maven.
     *
     * @param goals goals for the Maven
     * @return this
     */
    default MavenExecSpec goals(Iterable<String> goals) {
        if (goals != null) {
            CollectionUtils.addAll(getGoals(), goals);
        }
        return this;
    }

    /**
     * Sets the goals for the Maven.
     *
     * @param goals goals for the Maven
     * @return this
     */
    default MavenExecSpec setGoals(Iterable<String> goals) {
        getGoals().clear();
        return goals(goals);
    }

    /**
     * Returns the goals for the Maven. Defaults to an empty list.
     *
     * @return goals for the Maven
     */
    @Input
    Set<String> getGoals();

    /**
     * Command line options.
     *
     * @param options Options closure
     * @return this
     */
    default MavenExecSpec options(Closure<MavenOptions> options) {
        ClosureBackedAction.execute(getOptions(), options);
        return this;
    }

    @Input
    @Optional
    MavenOptions getOptions();

    @Override
    @Input
    default boolean isAlsoMake() {
        return getOptions().isAlsoMake();
    }

    @Override
    default void setAlsoMake(boolean alsoMake) {
        getOptions().setAlsoMake(alsoMake);
    }

    @Override
    @Input
    default boolean isAlsoMakeDependents() {
        return getOptions().isAlsoMakeDependents();
    }

    @Override
    default void setAlsoMakeDependents(boolean alsoMakeDependents) {
        getOptions().setAlsoMakeDependents(alsoMakeDependents);
    }

    @Override
    @Input
    default boolean isBatchMode() {
        return getOptions().isBatchMode();
    }

    @Override
    default void setBatchMode(boolean batchMode) {
        getOptions().setBatchMode(batchMode);
    }

    @Override
    @Input
    @Optional
    default String getBuilder() {
        return getOptions().getBuilder();
    }

    @Override
    default void setBuilder(String builder) {
        getOptions().setBuilder(builder);
    }

    @Override
    @Input
    default boolean isStrictChecksums() {
        return getOptions().isStrictChecksums();
    }

    @Override
    default void setStrictChecksums(boolean strictChecksums) {
        getOptions().setStrictChecksums(strictChecksums);
    }

    @Override
    @Input
    default boolean isLaxChecksums() {
        return getOptions().isLaxChecksums();
    }

    @Override
    default void setLaxChecksums(boolean laxChecksums) {
        getOptions().setLaxChecksums(laxChecksums);
    }

    @Override
    @Input
    @Optional
    default String getColor() {
        return getOptions().getColor();
    }

    @Override
    default void setColor(String color) {
        getOptions().setColor(color);
    }

    @Override
    @Input
    default boolean isCheckPluginUpdates() {
        return getOptions().isCheckPluginUpdates();
    }

    @Override
    default void setCheckPluginUpdates(boolean checkPluginUpdates) {
        getOptions().setCheckPluginUpdates(checkPluginUpdates);
    }

    @Override
    @Input
    @Optional
    default Map<String, String> getDefine() {
        return getOptions().getDefine();
    }

    @Override
    default void setDefine(Map<String, String> define) {
        getOptions().setDefine(define);
    }

    @Override
    @Input
    default boolean isErrors() {
        return getOptions().isErrors();
    }

    @Override
    default void setErrors(boolean errors) {
        getOptions().setErrors(errors);
    }

    @Override
    @Input
    @Optional
    default String getEncryptMasterPassword() {
        return getOptions().getEncryptMasterPassword();
    }

    @Override
    default void setEncryptMasterPassword(String encryptMasterPassword) {
        getOptions().setEncryptMasterPassword(encryptMasterPassword);
    }

    @Override
    @Input
    @Optional
    default String getEncryptPassword() {
        return getOptions().getEncryptPassword();
    }

    @Override
    default void setEncryptPassword(String encryptPassword) {
        getOptions().setEncryptPassword(encryptPassword);
    }

    @Override
    @InputFile
    @Optional
    default File getFile() {
        return getOptions().getFile();
    }

    @Override
    default void setFile(File file) {
        getOptions().setFile(file);
    }

    @Override
    @Input
    default boolean isFailAtEnd() {
        return getOptions().isFailAtEnd();
    }

    @Override
    default void setFailAtEnd(boolean failAtEnd) {
        getOptions().setFailAtEnd(failAtEnd);
    }

    @Override
    @Input
    default boolean isFailFast() {
        return getOptions().isFailFast();
    }

    @Override
    default void setFailFast(boolean failFast) {
        getOptions().setFailFast(failFast);
    }

    @Override
    @Input
    default boolean isFailNever() {
        return getOptions().isFailNever();
    }

    @Override
    default void setFailNever(boolean failNever) {
        getOptions().setFailNever(failNever);
    }

    @Override
    @InputFile
    @Optional
    default File getGlobalSettings() {
        return getOptions().getGlobalSettings();
    }

    @Override
    default void setGlobalSettings(File globalSettings) {
        getOptions().setGlobalSettings(globalSettings);
    }

    @Override
    @InputFile
    @Optional
    default File getGlobalToolchains() {
        return getOptions().getGlobalToolchains();
    }

    @Override
    default void setGlobalToolchains(File globalToolchains) {
        getOptions().setGlobalToolchains(globalToolchains);
    }

    @Override
    @Input
    default boolean isHelp() {
        return getOptions().isHelp();
    }

    @Override
    default void setHelp(boolean help) {
        getOptions().setHelp(help);
    }

    @Override
    @InputFile
    @Optional
    default File getLogFile() {
        return getOptions().getLogFile();
    }

    @Override
    default void setLogFile(File logFile) {
        getOptions().setLogFile(logFile);
    }

    @Override
    @Input
    default boolean isLegacyLocalRepository() {
        return getOptions().isLegacyLocalRepository();
    }

    @Override
    default void setLegacyLocalRepository(boolean legacyLocalRepository) {
        getOptions().setLegacyLocalRepository(legacyLocalRepository);
    }

    @Override
    @Input
    default boolean isNonRecursive() {
        return getOptions().isNonRecursive();
    }

    @Override
    default void setNonRecursive(boolean nonRecursive) {
        getOptions().setNonRecursive(nonRecursive);
    }

    @Override
    @Input
    default boolean isNoPluginRegistry() {
        return getOptions().isNoPluginRegistry();
    }

    @Override
    default void setNoPluginRegistry(boolean noPluginRegistry) {
        getOptions().setNoPluginRegistry(noPluginRegistry);
    }

    @Override
    @Input
    default boolean isNoPluginUpdates() {
        return getOptions().isNoPluginUpdates();
    }

    @Override
    default void setNoPluginUpdates(boolean noPluginUpdates) {
        getOptions().setNoPluginUpdates(noPluginUpdates);
    }

    @Override
    @Input
    default boolean isNoSnapshotUpdates() {
        return getOptions().isNoSnapshotUpdates();
    }

    @Override
    default void setNoSnapshotUpdates(boolean noSnapshotUpdates) {
        getOptions().setNoSnapshotUpdates(noSnapshotUpdates);
    }

    @Override
    @Input
    default boolean isNoTransferProgress() {
        return getOptions().isNoTransferProgress();
    }

    @Override
    default void setNoTransferProgress(boolean noTransferProgress) {
        getOptions().setNoTransferProgress(noTransferProgress);
    }

    @Override
    @Input
    default boolean isOffline() {
        return getOptions().isOffline();
    }

    @Override
    default void setOffline(boolean offline) {
        getOptions().setOffline(offline);
    }

    @Override
    @Input
    @Optional
    default String[] getActivateProfiles() {
        return getOptions().getActivateProfiles();
    }

    @Override
    default void setActivateProfiles(String[] activateProfiles) {
        getOptions().setActivateProfiles(activateProfiles);
    }

    @Override
    @Input
    @Optional
    default String[] getProjects() {
        return getOptions().getProjects();
    }

    @Override
    default void setProjects(String[] projects) {
        getOptions().setProjects(projects);
    }

    @Override
    @Input
    default boolean isQuiet() {
        return getOptions().isQuiet();
    }

    @Override
    default void setQuiet(boolean quiet) {
        getOptions().setQuiet(quiet);
    }

    @Override
    @Input
    @Optional
    default String getResumeFrom() {
        return getOptions().getResumeFrom();
    }

    @Override
    default void setResumeFrom(String resumeFrom) {
        getOptions().setResumeFrom(resumeFrom);
    }

    @Override
    @InputFile
    @Optional
    default File getSettings() {
        return getOptions().getSettings();
    }

    @Override
    default void setSettings(File settings) {
        getOptions().setSettings(settings);
    }

    @Override
    @InputFile
    @Optional
    default File getToolchains() {
        return getOptions().getToolchains();
    }

    @Override
    default void setToolchains(File toolchains) {
        getOptions().setToolchains(toolchains);
    }

    @Override
    @Input
    @Optional
    default String getThreads() {
        return getOptions().getThreads();
    }

    @Override
    default void setThreads(String threads) {
        getOptions().setThreads(threads);
    }

    @Override
    @Input
    default boolean isUpdateSnapshots() {
        return getOptions().isUpdateSnapshots();
    }

    @Override
    default void setUpdateSnapshots(boolean updateSnapshots) {
        getOptions().setUpdateSnapshots(updateSnapshots);
    }

    @Override
    @Input
    default boolean isUpdatePlugins() {
        return getOptions().isUpdatePlugins();
    }

    @Override
    default void setUpdatePlugins(boolean updatePlugins) {
        getOptions().setUpdatePlugins(updatePlugins);
    }

    @Override
    @Input
    default boolean isVersion() {
        return getOptions().isVersion();
    }

    @Override
    default void setVersion(boolean version) {
        getOptions().setVersion(version);
    }

    @Override
    @Input
    default boolean isShowVersion() {
        return getOptions().isShowVersion();
    }

    @Override
    default void setShowVersion(boolean showVersion) {
        getOptions().setShowVersion(showVersion);
    }

    @Override
    @Input
    default boolean isDebug() {
        return getOptions().isDebug();
    }

    @Override
    default void setDebug(boolean debug) {
        getOptions().setDebug(debug);
    }
}
