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

import org.assertj.core.api.ThrowableAssert;
import org.gradle.api.GradleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SuppressWarnings("OverloadMethodsDeclarationOrder")
class MavenHomeProviderTest {
    private static final String MVN = "mvn";
    private static final String BIN = "bin";
    private MavenHomeProvider provider;

    public static Stream<File> setIncorrectMavenHomeAsFile() throws Exception {
        final URL resource = MavenHomeProviderTest.class.getResource("");
        final Stream.Builder<File> builder = Stream.<File>builder()
                .add(null)
                .add(Path.of("").toFile())
                .add(Path.of(requireNonNull(resource).toURI()).toFile())
                .add(createTempFile());
        for (final String name : Arrays.asList(BIN, MVN, "mvn.bat", "mvn.cmd", "demo", "empty", "config")) {
            final File file1 = createTempDirectory().resolve(name).toFile();
            file1.createNewFile();
            builder.add(file1);
            final File directory = createTempDirectory().resolve(name).toFile();
            directory.mkdirs();
            builder.add(directory);
            if (!BIN.equals(name)) {
                builder.add(directory.toPath().resolve(name).toFile());
            }
        }
        final File file1 = createTempDirectory().resolve(BIN).resolve("m-v-n").toFile();
        file1.getParentFile().mkdirs();
        builder.add(file1);
        final File file2 = createTempDirectory().resolve(BIN).resolve(BIN).resolve(MVN).toFile();
        file2.getParentFile().mkdirs();
        builder.add(file2);
        return builder.build();
    }

    public static Stream<Arguments> setMavenHomeAsFile() throws IOException {
        final Stream.Builder<Arguments> builder = Stream.builder();
        final File file1 = createTempDirectory().resolve("mvnw").toFile();
        file1.createNewFile();
        builder.add(Arguments.of(file1, file1.getParentFile().toPath()));
        final File file2 = createTempDirectory().resolve("mvnw.cmd").toFile();
        file2.createNewFile();
        builder.add(Arguments.of(file2, file2.getParentFile().toPath()));
        for (final String name : Arrays.asList(MVN, "mvn.cmd", "mvn.bat")) {
            final File file = createTempDirectory().resolve(BIN).resolve(name).toFile();
            file.getParentFile().mkdir();
            file.createNewFile();
            final Path mavenHome = file.getParentFile().getParentFile().toPath();
            builder.add(Arguments.of(file, mavenHome));
            builder.add(Arguments.of(file.getParentFile(), mavenHome));
            builder.add(Arguments.of(file.getParentFile().getParentFile(), mavenHome));
        }
        return builder.build();
    }

    private static File createTempFile() throws IOException {
        return Files.createTempFile(null, null).toFile();
    }

    private static Path createTempDirectory() throws IOException {
        return Files.createTempDirectory(null);
    }

    @BeforeEach
    void setUp() {
        provider = spy(new MavenHomeProvider());
        assertThat(provider.getMavenHome()).isNull();
    }

    @AfterEach
    void tearDown() {
        provider = null;
    }

    @ParameterizedTest
    @MethodSource
    void setIncorrectMavenHomeAsFile(File directory) {
        assertIncorrectMavenHome(() -> provider.setMavenHome(directory), directory);
    }

    @ParameterizedTest
    @MethodSource
    void setMavenHomeAsFile(File path, Path expected) {
        provider.setMavenHome(path);
        assertMavenHome(expected);
    }

    @ParameterizedTest
    @MethodSource("setIncorrectMavenHomeAsFile")
    void setIncorrectMavenHomeAsPath(File directory) {
        final Path mavenHome = Optional.ofNullable(directory)
                .map(File::toPath)
                .orElse(null);
        assertIncorrectMavenHome(() -> provider.setMavenHome(mavenHome), directory);
    }

    @ParameterizedTest
    @MethodSource("setMavenHomeAsFile")
    void setMavenHomeAsPath(File path, Path expected) {
        provider.setMavenHome(path.toPath());
        assertMavenHome(expected);
    }

    @ParameterizedTest
    @MethodSource("setIncorrectMavenHomeAsFile")
    void setIncorrectMavenHomeAsString(File directory) {
        final String mavenHome = Optional.ofNullable(directory)
                .map(File::getAbsolutePath)
                .orElse(null);
        assertIncorrectMavenHome(() -> provider.setMavenHome(mavenHome), directory);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "     ",
            "\t\n ",
            "\n\n\n\n\n\n"
    })
    void setIncorrectMavenHomeAsBlankString(String directory) {
        assertIncorrectMavenHome(() -> provider.setMavenHome(directory), "blank");
    }

    @ParameterizedTest
    @MethodSource("setMavenHomeAsFile")
    void setMavenHomeAsString(File path, Path expected) {
        provider.setMavenHome(path.getAbsolutePath());
        assertMavenHome(expected);
    }

    @ParameterizedTest
    @MethodSource("setMavenHomeAsFile")
    void findMavenHome(File path, Path expected) {
        System.setProperty("maven.home", expected.toAbsolutePath().toString());
        assertThat(provider.findMavenHome()).isTrue();
        assertMavenHome(expected);
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "M2_HOME", matches = "^\\S+$")
    @DisabledIfEnvironmentVariable(named = "MAVEN_HOME", matches = "^\\S+$")
    void notFoundMavenHome() {
        System.clearProperty("maven.home");
        assertThat(provider.findMavenHome()).isFalse();
    }

    @Test
    void findMavenHomeIfDirectoryAlreadySet() throws Exception {
        final File mavenHome = createTempDirectory().resolve("mvnw").toFile();
        assertThat(mavenHome.createNewFile()).isTrue();
        provider.setMavenHome(mavenHome.getParentFile().toPath());
        assertThat(provider.findMavenHome()).isTrue();
    }

    private void assertIncorrectMavenHome(ThrowableAssert.ThrowingCallable callable, File directory) {
        assertIncorrectMavenHome(callable, Optional.ofNullable(directory).map(File::getName).orElse("blank"));
    }

    private void assertIncorrectMavenHome(ThrowableAssert.ThrowingCallable callable, String directory) {
        assertThatThrownBy(callable)
                .isInstanceOf(GradleException.class)
                .hasMessageStartingWith("Maven home is set to:")
                .hasMessageEndingWith("which is not a correct Maven installation directory")
                .hasMessageContaining(directory);
        assertThat(provider.getMavenHome()).isNull();
    }

    private void assertMavenHome(Path expected) {
        assertThat(provider.getMavenHome())
                .exists()
                .isDirectory()
                .isNotEmptyDirectory()
                .isEqualTo(expected);
        verify(provider, atLeastOnce()).setMavenHome(any(File.class));
    }
}
