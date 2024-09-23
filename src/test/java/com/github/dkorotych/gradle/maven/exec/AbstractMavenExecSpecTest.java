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

import com.github.dkorotych.gradle.maven.TestUtility;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static com.github.dkorotych.gradle.maven.TestUtility.random;
import static java.util.Arrays.asList;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("OverloadMethodsDeclarationOrder")
abstract class AbstractMavenExecSpecTest<T extends MavenExecSpec> {
    protected static Project project;
    protected T specification;

    @BeforeAll
    static void beforeAll() throws Exception {
        project = createProject();
    }

    public static List<File> mavenDir() {
        return asList(SystemUtils.getJavaIoTmpDir(), project.getProjectDir());
    }

    public static List<InputStream> standardInput() throws Exception {
        return asList(
                Files.newInputStream(project.getProjectDir().toPath().resolve("pom.xml")),
                new ByteArrayInputStream(new byte[0])
        );
    }

    public static List<OutputStream> standardOutput() throws Exception {
        return asList(
                Files.newOutputStream(project.getProjectDir().toPath().resolve("pom.xml")),
                new ByteArrayOutputStream()
        );
    }

    public static List<File> workingDirAsFile() {
        return asList(
                SystemUtils.getJavaIoTmpDir(),
                SystemUtils.getUserHome(),
                project.getProjectDir(),
                project.getLayout().getBuildDirectory().get().getAsFile()
        );
    }

    public static List<Map<String, ?>> environment() {
        return asList(
                of(),
                of("1", "2"),
                of(random(10), random(3), random(2), random(12))
        );
    }

    protected static Project createProject() throws Exception {
        final Project project = ProjectBuilder.builder().build();
        TestUtility.prepareProject(true, project.getProjectDir());
        return project;
    }

    @BeforeEach
    void setUp() {
        specification = create(project);
    }

    protected Pair<Project, T> create() throws Exception {
        final Project localProject = createProject();
        return Pair.of(localProject, create(localProject));
    }

    protected abstract T create(Project project);

    @Test
    void setExecutableAsString() {
        assertThatThrownBy(() -> specification.setExecutable(""))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void setExecutableAsObject() {
        assertThatThrownBy(() -> specification.setExecutable((Object) null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void executable() {
        assertThatThrownBy(() -> specification.executable(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void copyTo() {
        assertThatThrownBy(() -> specification.copyTo(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @ParameterizedTest
    @NullSource
    @MethodSource
    void mavenDir(File mavenDir) {
        specification.setMavenDir(mavenDir);
        assertThat(specification.getMavenDir()).isEqualTo(mavenDir);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void ignoreExitValue(boolean value) {
        specification.setIgnoreExitValue(value);
        assertThat(specification.isIgnoreExitValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource
    void standardInput(InputStream value) {
        specification.setStandardInput(value);
        assertThat(specification.getStandardInput()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource
    void standardOutput(OutputStream value) {
        specification.setStandardOutput(value);
        assertThat(specification.getStandardOutput()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource("standardOutput")
    void errorOutput(OutputStream value) {
        specification.setErrorOutput(value);
        assertThat(specification.getErrorOutput()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource
    void workingDirAsFile(File value) {
        specification.setWorkingDir(value);
        assertThat(specification.getWorkingDir()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource("workingDirAsFile")
    void workingDirAsObject(Object value) {
        specification.setWorkingDir(value);
        assertThat(specification.getWorkingDir()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource("workingDirAsFile")
    void workingDir(Object value) {
        specification.workingDir(value);
        assertThat(specification.getWorkingDir()).isEqualTo(value);
    }

    @Test
    void goalSetType() {
        assertThat(specification.getGoals()).isInstanceOf(LinkedHashSet.class);
    }
}
