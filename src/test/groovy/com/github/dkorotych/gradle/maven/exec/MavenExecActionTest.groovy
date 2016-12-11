package com.github.dkorotych.gradle.maven.exec

import org.gradle.process.internal.DefaultExecAction
import spock.lang.Unroll

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExecActionTest extends DefaultMavenExecSpecTest {
    @Unroll
    def "execute(). #os.familyName, #goals"() {
        setup:
        setOperatingSystem(os)
        DefaultExecAction delegate = Mock(DefaultExecAction)
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
