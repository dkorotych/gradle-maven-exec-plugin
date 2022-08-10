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
package com.github.dkorotych.gradle.maven.exec.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.os.OperatingSystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("MissingJavadocType")
public class PrepareMavenTask extends DefaultTask {
    private static final String CHARSET_NAME = Charset.forName(SystemUtils.FILE_ENCODING).name();
    private String version;
    private File outputDirectory;

    public static String createName(final String version) {
        return "prepareMaven_" + version;
    }

    @Input
    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
        final Project project = getProject();
        outputDirectory = project.getBuildDir().toPath().resolve("maven").resolve(version).toFile();
        assert outputDirectory.exists() || outputDirectory.mkdirs();
        getOutputs().dir(outputDirectory);
        project.getExtensions().getExtraProperties().set("maven-" + version, outputDirectory);
    }

    @TaskAction
    public void prepare() {
        try (PrintStream stream = new PrintStream(new File(outputDirectory, "pom.xml"), CHARSET_NAME)) {
            stream.println(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                            + "<project xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                            + "         xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
                            + "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0"
                            + " http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
                            + "    <modelVersion>4.0.0</modelVersion>\n"
                            + "    <groupId>com.github.lazybones</groupId>\n"
                            + "    <artifactId>app</artifactId>\n"
                            + "    <version>0.1-SNAPSHOT</version>\n"
                            + "</project>\n");
        } catch (Exception e) {
            throw new GradleException("Can't create test environment", e);
        }
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            getProject().exec(execSpec -> {
                execSpec.executable(getLocalMavenExecutable())
                        .workingDir(outputDirectory);
                execSpec.setStandardOutput(outputStream);
                execSpec.setErrorOutput(errorStream);
                execSpec.setArgs(Arrays.asList("wrapper:wrapper", "-Dmaven=" + version));
            }).assertNormalExitValue();
        } catch (Exception e) {
            String description = readMessage(errorStream);
            if (StringUtils.isBlank(description)) {
                description = readMessage(outputStream);
            }
            if (StringUtils.isBlank(description)) {
                description = e.getMessage();
            }
            throw new GradleException(description, e);
        }
    }

    private Object getLocalMavenExecutable() {
        final Project project = getProject();
        final String key = "maven";
        Object maven = Optional.of(project)
                .map(Project::getExtensions)
                .map(ExtensionContainer::getExtraProperties)
                .filter(properties -> properties.has(key))
                .map(properties -> properties.get(key))
                .orElse(null);
        if (maven == null) {
            maven = Optional.ofNullable(System.getenv("MAVEN_HOME"))
                    .filter(((Predicate<String>) s -> s.trim().isEmpty()).negate())
                    .map(Paths::get)
                    .map(path -> path.resolve("bin"))
                    .map(path -> path.resolve("mvn" + (OperatingSystem.current().isWindows() ? ".cmd" : "")))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .orElseThrow(() -> new GradleException(
                            "Maven installation not found by MAVEN_HOME environment variable"));
            project.getExtensions().getExtraProperties().set(key, maven);
        }
        return maven;
    }

    private String readMessage(final ByteArrayOutputStream stream) {
        try {
            return stream.toString(CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new GradleException("Can't read response from error stream", e);
        }
    }
}
