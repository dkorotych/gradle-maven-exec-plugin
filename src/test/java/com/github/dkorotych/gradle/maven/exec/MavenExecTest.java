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
import com.google.common.collect.ImmutableList;
import org.gradle.api.Project;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MavenExecTest extends AbstractMavenExecSpecTest<MavenExec> {
    private static MavenExec task;

    @BeforeAll
    static void beforeAll() throws Exception {
        project = createProject();
        task = createTask(project);
    }

    private static MavenExec createTask(Project project) {
        return project.getTasks().create(UUID.randomUUID().toString(), MavenExec.class);
    }

    @Override
    protected MavenExec create(Project project) {
        return task;
    }

    @ParameterizedTest
    @MethodSource
    void environment(Map<String, ?> value) {
        specification.environment(value);
        validateEnvironment(value);
    }

    @ParameterizedTest
    @MethodSource("environment")
    void setEnvironment(Map<String, ?> value) {
        specification.setEnvironment(value);
        validateEnvironment(value);
    }

    private void validateEnvironment(Map<String, ?> value) {
        assertThat(specification.getEnvironment()).containsAllEntriesOf(value);
    }

    @ParameterizedTest
    @MethodSource("environment")
    void environmentItem(Map<String, ?> value) {
        if (value != null && !value.isEmpty()) {
            final Map.Entry<String, ?> first = value.entrySet().iterator().next();
            specification.environment(first.getKey(), first.getValue());
            assertThat(specification.getEnvironment()).containsEntry(first.getKey(), first.getValue());
        }
    }

    @Test
    void commandLine() {
        assertThatThrownBy(() -> specification.commandLine(null, ""))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void testCommandLine() {
        assertThatThrownBy(() -> specification.commandLine(EMPTY_SET))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void args() {
        assertThatThrownBy(() -> specification.args(null, ""))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void testArgs() {
        assertThatThrownBy(() -> specification.args(EMPTY_SET))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void setArgs() {
        assertThatThrownBy(() -> specification.setArgs(EMPTY_SET))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void testSetArgs() {
        final List<String> list = emptyList();
        assertThatThrownBy(() -> specification.setArgs(list))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void setCommandLine() {
        assertThatThrownBy(() -> specification.setCommandLine(null, ""))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void testSetCommandLine() {
        assertThatThrownBy(() -> specification.setCommandLine(EMPTY_SET))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void testSetCommandLine1() {
        final List<String> list = emptyList();
        assertThatThrownBy(() -> specification.setCommandLine(list))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void setExecutable() {
        assertThatThrownBy(() -> specification.setExecutable(""))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void testSetExecutable() {
        assertThatThrownBy(() -> specification.setExecutable(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void testExecutable() {
        final Object o = new Object();
        assertThatThrownBy(() -> specification.executable(o))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void testCopyTo() {
        //noinspection ConstantConditions
        assertThatThrownBy(() -> specification.copyTo(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void exec() throws Exception {
        final Project project = createProject();
        MavenExec mavenExec = createTask(project);
        mavenExec.setWorkingDir(project.getProjectDir());
        final String validate = "validate";
        mavenExec.setGoals(singleton(validate));
        mavenExec.exec();
        assertThat(mavenExec.getCommandLine())
                .isEqualTo(TestUtility.commandLine(project.getProjectDir(), validate));

        mavenExec = createTask(project);
        mavenExec.setGoals(ImmutableList.of("clean", validate));
        mavenExec.setBatchMode(true);
        mavenExec.getOptions().setFailFast(true);
        mavenExec.getOptions().quiet(true);
        mavenExec.exec();
        assertThat(mavenExec.getCommandLine())
                .isEqualTo(TestUtility.commandLine(project.getProjectDir(),
                        "--batch-mode", "--fail-fast", "--quiet", "clean", validate));
    }
}
