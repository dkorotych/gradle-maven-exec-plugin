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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.gradle.util.internal.VersionNumber;

/**
 * Implementation of the plugin to support the launch Maven tasks.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
public class MavenExecPlugin implements Plugin<Project> {
    public static final String OPERATION_NAME = "exec-operations";
    private static final String CONVENTION_NAME = "mavenexec";

    @Override
    public void apply(final Project project) {
        final ExtraPropertiesExtension properties = project.getExtensions().getExtraProperties();
        properties.set(MavenExec.class.getSimpleName(), MavenExec.class);
        if (VersionNumber.parse(project.getGradle().getGradleVersion()).getMajor() < 9) {
            project.getConvention().getPlugins().put(CONVENTION_NAME, new MavenExecConvention(project));
        } else {
            properties.set(CONVENTION_NAME, new MavenExecConvention(project));
            properties.set(OPERATION_NAME, ExecOperationsInstanceHolder.getExecOperations(project));
        }
    }
}
