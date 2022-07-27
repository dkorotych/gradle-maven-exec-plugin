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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;
import org.gradle.api.Project;
import org.gradle.api.internal.project.DefaultProject;
import org.gradle.process.ExecSpec;
import org.gradle.process.internal.ExecAction;
import org.gradle.process.internal.ExecActionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mockito;

import java.util.Map;

import static com.github.dkorotych.gradle.maven.TestUtility.commandLine;
import static com.github.dkorotych.gradle.maven.TestUtility.createExecSpec;
import static org.assertj.core.api.Assertions.assertThat;
import static org.gradle.util.CollectionUtils.asCommandLine;

class MavenExecSpecDelegateTest extends AbstractMavenExecSpecTest<MavenExecSpecDelegate> {

    @ParameterizedTest
    @NullSource
    @MethodSource
    void environment(Map<String, ?> value) {
        specification.environment(value);
        validateEnvironment(value);
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("environment")
    void setEnvironment(Map<String, ?> value) {
        specification.setEnvironment(value);
        validateEnvironment(value);
    }

    private void validateEnvironment(Map<String, ?> value) {
        if (value != null) {
            assertThat(specification.getEnvironment()).isEqualTo(value);
        } else {
            assertThat(specification.getEnvironment())
                    .isNotNull()
                    .isEmpty();
        }
    }

    @ParameterizedTest
    @MethodSource("environment")
    void environmentItem(Map<String, ?> value) {
        if (value != null && !value.isEmpty()) {
            specification.setEnvironment(null);
            Map.Entry<String, ?> first = value.entrySet().iterator().next();
            specification.environment(first.getKey(), first.getValue());
            assertThat(specification.getEnvironment()).isEqualTo(ImmutableMap.of(first.getKey(), first.getValue()));
        }
    }

    @Test
    void getCommandLine() throws Exception {
        Pair<Project, MavenExecSpecDelegate> pair = create();
        assertThat(pair.getValue().getCommandLine())
                .isEqualTo(commandLine(pair.getKey().getProjectDir()));

        pair = create();
        MavenExecSpec delegate = pair.getValue();
        delegate.setGoals(ImmutableList.of("clean", "verify"));
        delegate.setDebug(true);
        delegate.getOptions().setOffline(true);
        delegate.getOptions().quiet(true);
        assertThat(pair.getValue().getCommandLine())
                .isEqualTo(commandLine(pair.getKey().getProjectDir(), "--debug", "--offline", "--quiet", "clean", "verify"));
    }

    @Test
    void getExecutable() throws Exception {
        final Pair<Project, MavenExecSpecDelegate> pair = create();
        assertThat(pair.getValue().getExecutable())
                .isEqualTo(asCommandLine(commandLine(pair.getKey().getProjectDir())));
    }

    @Override
    protected MavenExecSpecDelegate create(Project project) {
        return new MavenExecSpecDelegate(createExecSpec(project), project);
    }
}