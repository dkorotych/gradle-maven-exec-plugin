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
package com.github.dkorotych.gradle.maven.exec;

import com.github.dkorotych.gradle.maven.TestUtility;
import groovy.lang.Closure;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.process.internal.ExecException;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MavenExecConventionTest {
    private static Project project;

    @BeforeAll
    static void beforeAll() throws Exception {
        project = ProjectBuilder.builder()
                .build();
        TestUtility.prepareProject(true, project.getProjectDir());
    }

    @Test
    void nullProject() {
        assertThatThrownBy(() -> new MavenExecConvention(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Project should be not null");
    }

    @Test
    void nullSpecification() {
        assertThatThrownBy(() -> new MavenExecConvention(project).mavenexec(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Configure closure should not be null");
    }

    @Test
    void mavenexec() {
        assertThatNoException().isThrownBy(() -> execute(project, specification -> {
            specification.setGoals(Collections.singleton("validate"));
            specification.quiet(true);
        }));
    }

    @Test
    void error() {
        assertThatThrownBy(() -> execute(project, specification -> specification.quiet(true)))
                .isInstanceOf(ExecException.class)
                .hasMessageStartingWith("Process")
                .hasMessageEndingWith("finished with non-zero exit value 1");
    }

    @Test
    void error2() throws Exception {
        assertThatThrownBy(() -> execute(project, specification -> {
            specification.workingDir(project.getProjectDir());
            specification.mavenDir(project.file("config"))
                    .goals("validate")
                    .quiet(true);
        }))
                .isInstanceOf(GradleException.class)
                .hasMessageEndingWith("which is not a correct Maven installation directory");
    }

    @Test
    void error3() throws Exception {
        final Project localProject = ProjectBuilder.builder()
                .build();
        TestUtility.prepareProject(true, localProject.getProjectDir());
        assertThatThrownBy(() -> execute(localProject, specification -> {
            specification.workingDir(localProject.file("configuration"));
            specification.goals("validate")
                    .quiet(true);
        }))
                .isInstanceOf(GradleException.class)
                .hasMessageStartingWith("A problem occurred starting process");
    }

    private void execute(Project project, Consumer<MavenExecSpec> consumer) {
        final Closure<MavenExecSpec> closure = new Closure<MavenExecSpec>(project) {
            public Object doCall(MavenExecSpec it) {
                consumer.accept(it);
                return it;
            }
        };
        new MavenExecConvention(project).mavenexec(closure);
    }
}