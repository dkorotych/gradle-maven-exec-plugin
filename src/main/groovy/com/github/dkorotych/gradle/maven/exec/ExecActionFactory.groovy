/*
 * Copyright 2019 Dmitry Korotych
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

import org.gradle.api.internal.file.IdentityFileResolver
import org.gradle.process.internal.DefaultExecActionFactory
import org.gradle.process.internal.ExecAction
import org.gradle.util.GradleVersion

/**
 * Independent of the Gradle version builder of executor of some command.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class ExecActionFactory {
    private static final GradleVersion GRADLE_5_3 = GradleVersion.version('5.3')

    /**
     * Create new instance of execution action.
     *
     * @return Execution action
     */
    static ExecAction newInstance() {
        if (GradleVersion.current() < GRADLE_5_3) {
            return new DefaultExecActionFactory(new IdentityFileResolver()).newExecAction()
        }
        DefaultExecActionFactory.root().newExecAction()
    }
}
