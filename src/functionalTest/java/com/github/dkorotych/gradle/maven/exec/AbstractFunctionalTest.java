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
package com.github.dkorotych.gradle.maven.exec;

import org.gradle.api.JavaVersion;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.util.GradleVersion;
import org.gradle.util.internal.VersionNumber;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Objects.requireNonNull;

public abstract class AbstractFunctionalTest {
    private static final Collection<String> SUPPORTED_GRADLE_VERSIONS = Arrays.asList(
            "8.5",
            "8.6",
            "8.7",
            "8.8",
            "8.9",
            "8.10.2",
            "8.11.1",
            "8.12"
    );

    private static final GradleVersion MINIMAL_SUPPORTED_GRADLE_VERSION = GradleVersion.version("8.5");
    private static final Map<JavaVersion, GradleVersion> MINIMAL_SUPPORTED_VERSIONS;

    static {
        MINIMAL_SUPPORTED_VERSIONS = new HashMap<>();
        MINIMAL_SUPPORTED_VERSIONS.put(JavaVersion.VERSION_17, MINIMAL_SUPPORTED_GRADLE_VERSION);
        MINIMAL_SUPPORTED_VERSIONS.put(JavaVersion.VERSION_18, MINIMAL_SUPPORTED_GRADLE_VERSION);
        MINIMAL_SUPPORTED_VERSIONS.put(JavaVersion.VERSION_19, MINIMAL_SUPPORTED_GRADLE_VERSION);
        MINIMAL_SUPPORTED_VERSIONS.put(JavaVersion.VERSION_20, MINIMAL_SUPPORTED_GRADLE_VERSION);
        MINIMAL_SUPPORTED_VERSIONS.put(JavaVersion.VERSION_21, MINIMAL_SUPPORTED_GRADLE_VERSION);
        MINIMAL_SUPPORTED_VERSIONS.put(JavaVersion.toVersion("22"), GradleVersion.version("8.7"));
        MINIMAL_SUPPORTED_VERSIONS.put(JavaVersion.toVersion("23"), GradleVersion.version("8.10.2"));
    }

    public static Collection<String> supportedGradleVersion() {
        final GradleVersion minimalGradleVersion = GradleVersion.version(minimalSupportedGradleVersion());
        final GradleVersion maximalGradleVersion = GradleVersion.version(maximalSupportedGradleVersion());
        return SUPPORTED_GRADLE_VERSIONS.stream()
                .map(GradleVersion::version)
                .filter(version -> version.compareTo(minimalGradleVersion) >= 0)
                .filter(version -> version.compareTo(maximalGradleVersion) <= 0)
                .map(GradleVersion::getVersion)
                .toList();
    }

    public static Collection<String> supportedMavenVersion() {
        try {
            final URL resource = AbstractFunctionalTest.class.getResource("/");
            final Path dir = Path.of(Objects.requireNonNull(resource).toURI())
                    .getParent()
                    .getParent()
                    .getParent()
                    .resolve("maven");
            final List<String> versions = new ArrayList<>();
            try (DirectoryStream<Path> paths = Files.newDirectoryStream(dir, entry -> entry.toFile().isDirectory())) {
                for (Path path : paths) {
                    versions.add(path.toAbsolutePath().toString());
                }
            }
            versions.sort(Comparator.naturalOrder());
            return versions;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String maximalSupportedGradleVersion() {
        return toString(SUPPORTED_GRADLE_VERSIONS.stream()
                .map(VersionNumber::parse)
                .max(Comparator.naturalOrder()));
    }

    public static String minimalSupportedGradleVersion() {
        final JavaVersion jdkVersion = JavaVersion.current();
        final GradleVersion min = MINIMAL_SUPPORTED_VERSIONS.getOrDefault(jdkVersion, MINIMAL_SUPPORTED_GRADLE_VERSION);
        return toString(SUPPORTED_GRADLE_VERSIONS.stream()
                .map(VersionNumber::parse)
                .filter(versionNumber -> {
                    final GradleVersion gradleVersion = GradleVersion.version(versionNumber.toString());
                    return gradleVersion.compareTo(min) >= 0;
                })
                .min(Comparator.naturalOrder()));
    }

    public static String latestMavenVersion() {
        final String latestVersion = supportedMavenVersion().stream()
                .map(Paths::get)
                .map(Path::toFile)
                .map(File::getName)
                .map(VersionNumber::parse)
                .max(Comparator.naturalOrder())
                .map(VersionNumber::toString)
                .orElseThrow(RuntimeException::new);
        return supportedMavenVersion().stream()
                .filter(path -> path.endsWith(latestVersion))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    protected static void prepareProject(final File projectDir) throws Exception {
        final URL resource = MavenExecPluginFunctionalTest.class.getResource("/fixtures/versions");
        final File source = Path.of(requireNonNull(resource).toURI()).toFile();
        FileUtils.copyDirectory(source, projectDir);
    }

    protected BuildResult execute(File projectDir, String gradleVersion, String mavenVersion, String... commandLine) {
        if (mavenVersion != null) {
            System.setProperty("maven-home", mavenVersion);
        }

        final boolean localRun = Boolean.parseBoolean(System.getenv("CI"));
        final ArrayList<String> arguments = new ArrayList<>(Arrays.asList(commandLine));
        if (localRun) {
            arguments.add("--warning-mode");
            arguments.add("all");
        }

        final GradleRunner runner = GradleRunner.create();
        runner.forwardStdOutput(new StringWriter());
        runner.withPluginClasspath();
        runner.withArguments(arguments);
        runner.withProjectDir(projectDir);
        runner.withGradleVersion(gradleVersion);
        runner.withDebug(localRun);
        return runner.build();
    }

    private static String toString(Optional<VersionNumber> optionalVersionNumber) {
        return optionalVersionNumber
                .map(number -> {
                    if (number.getMicro() == 0) {
                        return number.getMajor() + "." + number.getMinor();
                    }
                    return number.toString();
                })
                .orElseThrow(RuntimeException::new);
    }
}
