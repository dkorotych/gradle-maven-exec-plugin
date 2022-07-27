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
package com.github.dkorotych.gradle.maven;

import org.assertj.core.api.Assertions;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.internal.impldep.org.apache.commons.lang.SystemUtils;
import org.gradle.internal.os.OperatingSystem;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.github.dkorotych.gradle.maven.TestUtility.*;
import static org.junit.jupiter.params.ParameterizedTest.DEFAULT_DISPLAY_NAME;

class MavenExecutableProviderTest {

    public static Stream<Arguments> getExecutable() throws Exception {
        final Stream.Builder<Arguments> builder = Stream.builder();
        for (final boolean withWrapper : Arrays.asList(true, false)) {
            final File projectDir = Files.createTempDirectory(null).toFile();
            TestUtility.prepareProject(withWrapper, projectDir);

            for (OperatingSystem os : operatingSystems()) {
                for (File path : Arrays.asList(null, SystemUtils.getUserHome(), SystemUtils.getJavaIoTmpDir(), projectDir)) {
                    final boolean hasWrapper = withWrapper && projectDir.equals(path);
                    List<String> commandLine = commandLine(path, os, false, hasWrapper);
                    builder.add(Arguments.of(Optional.ofNullable(path).map(File::getPath).orElse(null), os, String.join(" ", commandLine)));
                }
            }
        }
        return builder.build();
    }

    @ParameterizedTest(name = DEFAULT_DISPLAY_NAME)
    @MethodSource
    void getExecutable(Path directory, OperatingSystem operatingSystem, String expected) {
        setOperatingSystem(operatingSystem);
        final MavenExecutableProvider provider = new MavenExecutableProvider(directory);
        Assertions.assertThat(provider.getExecutable()).isEqualTo(expected);
    }
}