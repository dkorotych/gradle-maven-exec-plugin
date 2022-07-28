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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.gradle.api.GradleException;
import org.gradle.api.Project;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Description for the particular version of Maven placed in a certain directory.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
public class MavenDescriptor {
    private static final String CHARSET = Charset.forName(SystemUtils.FILE_ENCODING).name();

    private final Project project;
    private final MavenExecutableProvider executableProvider;
    private final Supplier<String> versionSupplier;
    private final Supplier<Set<String>> supportedOptionsSupplier;

    /**
     * Create a new descriptor.
     *
     * @param mavenHome Maven installation directory
     * @param project   Current Gradle project
     */
    public MavenDescriptor(final Path mavenHome, final Project project) {
        this.project = project;
        executableProvider = new MavenExecutableProvider(mavenHome);
        versionSupplier = MemoizedSupplier.of(() -> parseVersion(execute("--version")));
        supportedOptionsSupplier = MemoizedSupplier.of(() -> parseSupportedOptions(execute("--help")));
    }

    /**
     * Input file is a Maven execution file?
     *
     * @param file Input file
     * @return true, if input filename is look like a Maven execution file, false - otherwise
     */
    public static boolean isMavenExecutionFile(final File file) {
        return Optional.ofNullable(file)
                .filter(File::isFile)
                .map(File::getName)
                .filter(name -> "mvn".equals(name) || "mvn.cmd".equals(name) || "mvn.bat".equals(name))
                .isPresent();
    }

    /**
     * Input file is a Maven wrapper file?
     *
     * @param file Input file
     * @return true, if input filename is look like a Maven wrapper file, false - otherwise
     */
    public static boolean isMavenExecutionWrapperFile(final File file) {
        return Optional.ofNullable(file)
                .filter(File::isFile)
                .map(File::getName)
                .filter(name -> "mvnw".equals(name) || "mvnw.cmd".equals(name))
                .isPresent();
    }

    /**
     * Get Maven application version.
     *
     * @return Maven version
     */
    public String getVersion() {
        return versionSupplier.get();
    }

    /**
     * Get supported options of current Maven installation.
     *
     * @return Options
     */
    public Set<String> getSupportedOptions() {
        return supportedOptionsSupplier.get();
    }

    /**
     * Absolute path to current Maven execution command.
     *
     * @return Maven executable command
     * @see MavenExecutableProvider#getExecutable()
     */
    public String getExecutable() {
        return executableProvider.getExecutable();
    }

    private String parseVersion(final InputStream stream) {
        final Pattern pattern = Pattern.compile("^\\QApache Maven \\E(\\S+)(?:.+)?$");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, UTF_8))) {
            return reader.lines()
                    .map(pattern::matcher)
                    .filter(Matcher::find)
                    .map(matcher -> matcher.group(1))
                    .findAny()
                    .orElse(null);
        } catch (IOException e) {
            throw new GradleException("Can't parse Maven version", e);
        }
    }

    private Set<String> parseSupportedOptions(final InputStream stream) {
        final Pattern pattern = Pattern.compile("^\\s+(?:-\\w+,)?(--\\S+).+$");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, UTF_8))) {
            return reader.lines()
                    .map(pattern::matcher)
                    .filter(Matcher::find)
                    .map(matcher -> matcher.group(1))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new GradleException("Can't parse Maven supported options", e);
        }
    }

    InputStream execute(final String option) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        try {
            project.exec(execSpec -> {
                execSpec.executable(getExecutable())
                        .workingDir(SystemUtils.getJavaIoTmpDir());
                execSpec.setStandardOutput(outputStream);
                execSpec.setErrorOutput(errorStream);
                execSpec.setArgs(Collections.singletonList(option));
            }).assertNormalExitValue();
        } catch (Exception e) {
            String description = readMessage(errorStream);
            if (StringUtils.isBlank(description)) {
                description = e.getMessage();
            }
            throw new GradleException(description, e);
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private String readMessage(final ByteArrayOutputStream stream) {
        try {
            return stream.toString(CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new GradleException("Can't read response from error stream", e);
        }
    }
}
