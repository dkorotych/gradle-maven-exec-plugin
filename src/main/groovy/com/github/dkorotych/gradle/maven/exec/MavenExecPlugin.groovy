/*
 * Copyright 2016 Dmitry Korotych
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
package com.github.dkorotych.gradle.maven.exec

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.IdentityFileResolver
import org.gradle.process.internal.DefaultExecActionFactory
import org.gradle.process.internal.ExecAction

/**
 * Implementation of the plugin to support the launch Maven tasks.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecPlugin implements Plugin<Project> {

    /**
     * Apply this plugin to the project.
     *
     * @param project Current project
     */
    @Override
    void apply(Project project) {
        project.extensions.extraProperties.set(MavenExec.simpleName, MavenExec)
        ExecAction action = new DefaultExecActionFactory(new IdentityFileResolver()).newExecAction()
        MavenExecPluginProjectConvention convention = new MavenExecPluginProjectConvention(action)
        project.convention.plugins['mavenexec'] = convention
    }
}
