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

public interface MavenOptions {
    boolean isAlsoMake();

    default boolean alsoMake() {
        return isAlsoMake();
    }

    void setAlsoMake(boolean alsoMake);

    default void alsoMake(boolean alsoMake) {
        setAlsoMake(alsoMake);
    }

    boolean isAlsoMakeDependents();

    default boolean alsoMakeDependents() {
        return isAlsoMakeDependents();
    }

    void setAlsoMakeDependents(boolean alsoMakeDependents);

    default void alsoMakeDependents(boolean alsoMakeDependents) {
        setAlsoMakeDependents(alsoMakeDependents);
    }

    boolean isBatchMode();

    default boolean batchMode() {
        return isBatchMode();
    }

    void setBatchMode(boolean batchMode);

    default void batchMode(boolean batchMode) {
        setBatchMode(batchMode);
    }

    String getBuilder();

    default String builder() {
        return getBuilder();
    }

    void setBuilder(String builder);

    default void builder(String builder) {
        setBuilder(builder);
    }

    boolean isStrictChecksums();

    default boolean strictChecksums() {
        return isStrictChecksums();
    }

    void setStrictChecksums(boolean strictChecksums);

    default void strictChecksums(boolean strictChecksums) {
        setStrictChecksums(strictChecksums);
    }

    boolean isLaxChecksums();

    default boolean laxChecksums() {
        return isLaxChecksums();
    }

    void setLaxChecksums(boolean laxChecksums);

    default void laxChecksums(boolean laxChecksums) {
        setLaxChecksums(laxChecksums);
    }

    String getColor();

    default String color() {
        return getColor();
    }

    void setColor(String color);

    default void color(String color) {
        setColor(color);
    }

    boolean isCheckPluginUpdates();

    default boolean checkPluginUpdates() {
        return isCheckPluginUpdates();
    }

    void setCheckPluginUpdates(boolean checkPluginUpdates);

    default void checkPluginUpdates(boolean checkPluginUpdates) {
        setCheckPluginUpdates(checkPluginUpdates);
    }

    Map<String, String> getDefine();

    default Map<String, String> define() {
        return getDefine();
    }

    void setDefine(Map<String, String> define);

    default void define(Map<String, String> define) {
        setDefine(define);
    }

    boolean isErrors();

    default boolean errors() {
        return isErrors();
    }

    void setErrors(boolean errors);

    default void errors(boolean errors) {
        setErrors(errors);
    }

    String getEncryptMasterPassword();

    default String encryptMasterPassword() {
        return getEncryptMasterPassword();
    }

    void setEncryptMasterPassword(String encryptMasterPassword);

    default void encryptMasterPassword(String encryptMasterPassword) {
        setEncryptMasterPassword(encryptMasterPassword);
    }

    String getEncryptPassword();

    default String encryptPassword() {
        return getEncryptPassword();
    }

    void setEncryptPassword(String encryptPassword);

    default void encryptPassword(String encryptPassword) {
        setEncryptPassword(encryptPassword);
    }

    File getFile();

    default File file() {
        return getFile();
    }

    void setFile(File file);

    default void file(File file) {
        setFile(file);
    }

    boolean isFailAtEnd();

    default boolean failAtEnd() {
        return isFailAtEnd();
    }

    void setFailAtEnd(boolean failAtEnd);

    default void failAtEnd(boolean failAtEnd) {
        setFailAtEnd(failAtEnd);
    }

    boolean isFailFast();

    default boolean failFast() {
        return isFailFast();
    }

    void setFailFast(boolean failFast);

    default void failFast(boolean failFast) {
        setFailFast(failFast);
    }

    boolean isFailNever();

    default boolean failNever() {
        return isFailNever();
    }

    void setFailNever(boolean failNever);

    default void failNever(boolean failNever) {
        setFailNever(failNever);
    }

    File getGlobalSettings();

    default File globalSettings() {
        return getGlobalSettings();
    }

    void setGlobalSettings(File globalSettings);

    default void globalSettings(File globalSettings) {
        setGlobalSettings(globalSettings);
    }

    File getGlobalToolchains();

    default File globalToolchains() {
        return getGlobalToolchains();
    }

    void setGlobalToolchains(File globalToolchains);

    default void globalToolchains(File globalToolchains) {
        setGlobalToolchains(globalToolchains);
    }

    boolean isHelp();

    default boolean help() {
        return isHelp();
    }

    void setHelp(boolean help);

    default void help(boolean help) {
        setHelp(help);
    }

    File getLogFile();

    default File logFile() {
        return getLogFile();
    }

    void setLogFile(File logFile);

    default void logFile(File logFile) {
        setLogFile(logFile);
    }

    boolean isLegacyLocalRepository();

    default boolean legacyLocalRepository() {
        return isLegacyLocalRepository();
    }

    void setLegacyLocalRepository(boolean legacyLocalRepository);

    default void legacyLocalRepository(boolean legacyLocalRepository) {
        setLegacyLocalRepository(legacyLocalRepository);
    }

    boolean isNonRecursive();

    default boolean nonRecursive() {
        return isNonRecursive();
    }

    void setNonRecursive(boolean nonRecursive);

    default void nonRecursive(boolean nonRecursive) {
        setNonRecursive(nonRecursive);
    }

    boolean isNoPluginRegistry();

    default boolean noPluginRegistry() {
        return isNoPluginRegistry();
    }

    void setNoPluginRegistry(boolean noPluginRegistry);

    default void noPluginRegistry(boolean noPluginRegistry) {
        setNoPluginRegistry(noPluginRegistry);
    }

    boolean isNoPluginUpdates();

    default boolean noPluginUpdates() {
        return isNoPluginUpdates();
    }

    void setNoPluginUpdates(boolean noPluginUpdates);

    default void noPluginUpdates(boolean noPluginUpdates) {
        setNoPluginUpdates(noPluginUpdates);
    }

    boolean isNoSnapshotUpdates();

    default boolean noSnapshotUpdates() {
        return isNoSnapshotUpdates();
    }

    void setNoSnapshotUpdates(boolean noSnapshotUpdates);

    default void noSnapshotUpdates(boolean noSnapshotUpdates) {
        setNoSnapshotUpdates(noSnapshotUpdates);
    }

    boolean isNoTransferProgress();

    default boolean noTransferProgress() {
        return isNoTransferProgress();
    }

    void setNoTransferProgress(boolean noTransferProgress);

    default void noTransferProgress(boolean noTransferProgress) {
        setNoTransferProgress(noTransferProgress);
    }

    boolean isOffline();

    default boolean offline() {
        return isOffline();
    }

    void setOffline(boolean offline);

    default void offline(boolean offline) {
        setOffline(offline);
    }

    String[] getActivateProfiles();

    default String[] activateProfiles() {
        return getActivateProfiles();
    }

    void setActivateProfiles(String[] activateProfiles);

    default void activateProfiles(String[] activateProfiles) {
        setActivateProfiles(activateProfiles);
    }

    String[] getProjects();

    default String[] projects() {
        return getProjects();
    }

    void setProjects(String[] projects);

    default void projects(String[] projects) {
        setProjects(projects);
    }

    boolean isQuiet();

    default boolean quiet() {
        return isQuiet();
    }

    void setQuiet(boolean quiet);

    default void quiet(boolean quiet) {
        setQuiet(quiet);
    }

    String getResumeFrom();

    default String resumeFrom() {
        return getResumeFrom();
    }

    void setResumeFrom(String resumeFrom);

    default void resumeFrom(String resumeFrom) {
        setResumeFrom(resumeFrom);
    }

    File getSettings();

    default File settings() {
        return getSettings();
    }

    void setSettings(File settings);

    default void settings(File settings) {
        setSettings(settings);
    }

    File getToolchains();

    default File toolchains() {
        return getToolchains();
    }

    void setToolchains(File toolchains);

    default void toolchains(File toolchains) {
        setToolchains(toolchains);
    }

    String getThreads();

    default String threads() {
        return getThreads();
    }

    void setThreads(String threads);

    default void threads(String threads) {
        setThreads(threads);
    }

    boolean isUpdateSnapshots();

    default boolean updateSnapshots() {
        return isUpdateSnapshots();
    }

    void setUpdateSnapshots(boolean updateSnapshots);

    default void updateSnapshots(boolean updateSnapshots) {
        setUpdateSnapshots(updateSnapshots);
    }

    boolean isUpdatePlugins();

    default boolean updatePlugins() {
        return isUpdatePlugins();
    }

    void setUpdatePlugins(boolean updatePlugins);

    default void updatePlugins(boolean updatePlugins) {
        setUpdatePlugins(updatePlugins);
    }

    boolean isVersion();

    default boolean version() {
        return isVersion();
    }

    void setVersion(boolean version);

    default void version(boolean version) {
        setVersion(version);
    }

    boolean isShowVersion();

    default boolean showVersion() {
        return isShowVersion();
    }

    void setShowVersion(boolean showVersion);

    default void showVersion(boolean showVersion) {
        setShowVersion(showVersion);
    }

    boolean isDebug();

    default boolean debug() {
        return isDebug();
    }

    void setDebug(boolean debug);

    default void debug(boolean debug) {
        setDebug(debug);
    }
}
