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

import org.apache.commons.lang3.SystemUtils;
import org.gradle.api.Project;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("OverloadMethodsDeclarationOrder")
class MavenDescriptorTest {

    public static Stream<Arguments> getVersion() throws Exception {
        return descriptorFixtures().stream()
                .map(directory -> {
                    try {
                        return Arguments.of(directory.getName(), getVersion(directory));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public static Stream<Arguments> getSupportedOptions() throws Exception {
        return descriptorFixtures().stream()
                .map(directory -> {
                    try {
                        return Arguments.of(directory.getName(), getHelp(directory), getOptions(directory));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public static Collection<File> executeArguments() throws Exception {
        final ArrayList<File> list = new ArrayList<>();
        list.add(SystemUtils.getJavaIoTmpDir());
        list.add(SystemUtils.getUserHome());
        list.add(Files.createTempDirectory(null).toFile());
        Project project = ProjectBuilder.builder().build();
        TestUtility.prepareProject(true, project.getProjectDir());
        list.add(project.getProjectDir());
        list.add(project.getRootDir());
        project = ProjectBuilder.builder().build();
        TestUtility.prepareProject(false, project.getProjectDir());
        list.add(project.getProjectDir());
        list.add(project.getRootDir());
        project = ProjectBuilder.builder().build();
        TestUtility.prepareProject(true, project.getLayout().getBuildDirectory().getAsFile().get());
        list.add(project.getLayout().getBuildDirectory().getAsFile().get());
        project = ProjectBuilder.builder().build();
        TestUtility.prepareProject(false, project.getLayout().getBuildDirectory().getAsFile().get());
        list.add(project.getLayout().getBuildDirectory().getAsFile().get());
        return list;
    }

    private static List<File> descriptorFixtures() throws Exception {
        final URL resource = MavenDescriptorTest.class.getResource("/fixtures/descriptor");
        final Path path = Paths.get(requireNonNull(resource).toURI());
        try (Stream<Path> paths = Files.list(path)) {
            return paths.map(Path::toFile)
                    .filter(File::isDirectory)
                    .sorted(Comparator.comparing(File::getName))
                    .toList();
        }
    }

    private static String getVersion(File directory) throws IOException {
        return getText(directory, "version.txt");
    }

    private static String getHelp(File directory) throws IOException {
        return getText(directory, "help.txt");
    }

    private static Collection<String> getOptions(File directory) throws Exception {
        try (Stream<String> lines = Files.lines(resolve(directory, "options.txt").toPath(), UTF_8)) {
            return lines.toList();
        }
    }

    private static String getText(File directory, String fileName) throws IOException {
        return FileUtils.readFileToString(resolve(directory, fileName), UTF_8);
    }

    private static File resolve(File directory, String fileName) {
        return directory.toPath()
                .resolve(fileName)
                .normalize()
                .toAbsolutePath()
                .toFile();
    }

    @ParameterizedTest(name = "getVersion({0})")
    @MethodSource
    void getVersion(String version, String text) {
        final MavenDescriptor descriptor = createMavenDescriptor("--version", text);
        assertThat(descriptor.getVersion())
                .isNotBlank()
                .isEqualTo(version);
        verify(descriptor).getVersion();
        verify(descriptor).execute(anyString());
        descriptor.getVersion();
        verify(descriptor, times(2)).getVersion();
        verify(descriptor).execute(anyString());
        verifyNoMoreInteractions(descriptor);
    }

    @ParameterizedTest(name = "getSupportedOptions({0})")
    @MethodSource
    void getSupportedOptions(String version, String text, Collection<String> options) {
        final MavenDescriptor descriptor = createMavenDescriptor("--help", text);
        assertThat(descriptor.getSupportedOptions())
                .isNotEmpty()
                .doesNotHaveDuplicates()
                .containsAll(options);
        verify(descriptor).getSupportedOptions();
        verify(descriptor).execute(anyString());
        descriptor.getSupportedOptions();
        verify(descriptor, times(2)).getSupportedOptions();
        verify(descriptor).execute(anyString());
        verifyNoMoreInteractions(descriptor);
    }

    @Test
    void execute() throws Exception {
        validate(project -> new MavenDescriptor(project.getProjectDir().toPath(), project.getProjectDir(), project));
    }

    @ParameterizedTest
    @MethodSource("executeArguments")
    void execute(File workingDir) throws Exception {
        validate(project -> new MavenDescriptor(project.getProjectDir().toPath(), workingDir, project));
    }

    @Test
    void executeWithDefaultWorkingDirectory() throws Exception {
        validate(project -> new MavenDescriptor(project.getProjectDir().toPath(), project));
    }

    @Test
    void help() throws Exception {
        final Project project = ProjectBuilder.builder().build();
        final File projectDir = project.getProjectDir();
        TestUtility.prepareProject(true, projectDir);
        final MavenDescriptor descriptor = new MavenDescriptor(projectDir.toPath(), projectDir, project);
        assertThat(descriptor.getSupportedOptions())
                .isNotEmpty()
                .doesNotHaveDuplicates()
                .allMatch(option -> option.startsWith("--"))
                .doesNotContain("--color");
    }

    private MavenDescriptor createMavenDescriptor(String options, String text) {
        final MavenDescriptor descriptor = mock(MavenDescriptor.class, withSettings()
                .useConstructor(null, null, null)
                .defaultAnswer(Answers.CALLS_REAL_METHODS));
        final ByteArrayInputStream response = new ByteArrayInputStream(text.getBytes());
        doReturn(response).when(descriptor).execute(options);
        return descriptor;
    }

    private void validate(Function<Project, MavenDescriptor> function) throws Exception {
        final Project project = ProjectBuilder.builder().build();
        TestUtility.prepareProject(true, project.getProjectDir());
        final MavenDescriptor descriptor = function.apply(project);
        assertThat(descriptor.getVersion())
                .isNotBlank()
                .isEqualTo("3.0");
    }
}
