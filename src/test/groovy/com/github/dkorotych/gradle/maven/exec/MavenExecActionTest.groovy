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

import spock.lang.Unroll

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecActionTest extends DefaultMavenExecSpecTest {
    @Unroll
    def "execute(). #os.familyName, #goals"() {
        setup:
        setOperatingSystem(os)
        registerMavenDescriptorMock()
        def delegate = registerDefaultExecActionMock()
        MavenExecAction action = new MavenExecAction(delegate)
        action.setGoals(goals)

        when:
        action.execute()

        then:
        1 * delegate.setCommandLine(commandLine)
        1 * delegate.execute()
        0 * delegate._

        where:
        [goals, os, commandLine] << setGoalsDataProvider()
    }
}
