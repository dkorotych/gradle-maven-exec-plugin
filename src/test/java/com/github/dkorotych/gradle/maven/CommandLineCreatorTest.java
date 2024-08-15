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

import com.github.dkorotych.gradle.maven.exec.MavenExecSpec;
import com.github.dkorotych.gradle.maven.exec.MavenExecSpecDelegate;
import org.gradle.api.Project;
import org.gradle.process.ExecSpec;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.github.dkorotych.gradle.maven.TestUtility.*;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("OverloadMethodsDeclarationOrder")
class CommandLineCreatorTest {
    private static Project project;

    @BeforeAll
    static void beforeAll() throws Exception {
        project = ProjectBuilder.builder()
                .build();
        TestUtility.prepareProject(true, project.getProjectDir());
    }

    public static Stream<Arguments> validate() {
        final ExecSpec delegate = createExecSpec(project);
        final MavenExecSpecDelegate delegate1 = new MavenExecSpecDelegate(delegate, project);
        delegate1.setOffline(true);
        delegate1.setUpdatePlugins(true);
        delegate1.setGoals(of("verify"));
        return Stream.of(
                Arguments.of(new MavenExecSpecDelegate(delegate, project), expectedCommandLine()),
                Arguments.of(delegate1, expectedCommandLine("--offline", "--update-plugins", "verify"))
        );
    }

    private static List<String> expectedCommandLine(String... arguments) {
        return commandLine(project.getProjectDir(), arguments);
    }

    @Test
    void nullSpecification() {
        assertThatThrownBy(() -> new CommandLineCreator(null, project))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Specification should be not null");
    }

    @Test
    void nullProject() {
        assertThatThrownBy(() ->
                project.exec(execSpec ->
                        new CommandLineCreator(new MavenExecSpecDelegate(execSpec, project), null)))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Project should be not null");
    }

    @ParameterizedTest
    @MethodSource
    void validate(MavenExecSpec specification, List<String> expected) {
        final CommandLineCreator creator = new CommandLineCreator(specification, project);
        final ArrayList<String> commandLine = new ArrayList<>();
        commandLine.add(creator.getExecutable());
        commandLine.addAll(creator.getArguments());
        assertThat(asCommandLine(commandLine))
                .isEqualTo(asCommandLine(expected));
    }
}
