package com.github.dkorotych.gradle.maven.exec

import com.github.dkorotych.gradle.maven.cli.MavenCli
import groovy.transform.TypeChecked
import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.os.OperatingSystem

/**
 * Implementation of the task to support the launch Maven.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExec extends AbstractExecTask<MavenExec> implements MavenExecSpec {
    private final boolean windows
    private final MavenCli mavenCli = new MavenCli()
    private final Set<String> goalSet = []
    private File installDir

    /**
     * Create implementation of the task.
     */
    MavenExec() {
        super(MavenExec)
        windows = OperatingSystem.current().isWindows()
    }

    @Override
    @TaskAction
    protected void exec() {
        // build correct command line
        prepareCommandLine()
        super.exec()
    }

    @Override
    void setExecutable(Object executable) {
        throw new UnsupportedOperationException()
    }

    @Override
    MavenExec executable(Object executable) {
        throw new UnsupportedOperationException()
    }

    @Override
    void setCommandLine(Iterable<?> args) {
        throw new UnsupportedOperationException()
    }

    @Override
    void setCommandLine(Object... args) {
        throw new UnsupportedOperationException()
    }

    @Override
    MavenExec args(Object... args) {
        throw new UnsupportedOperationException()
    }

    @Override
    MavenExec args(Iterable<?> args) {
        throw new UnsupportedOperationException()
    }

    @Override
    MavenExec setArgs(Iterable<?> arguments) {
        throw new UnsupportedOperationException()
    }

    @Override
    File getMavenDir() {
        installDir
    }

    @Override
    void setMavenDir(File dir) {
        installDir = dir
    }

    @Override
    MavenExecSpec mavenDir(File dir) {
        installDir = dir
        this
    }

    @Override
    MavenExecSpec goals(String... goals) {
        if (goals) {
            goalSet.addAll(goals)
        }
        this
    }

    @Override
    MavenExecSpec goals(Iterable<String> goals) {
        if (goals) {
            goalSet.addAll(goals)
        }
        this
    }

    @Override
    MavenExecSpec setGoals(Iterable<String> goals) {
        if (goals) {
            goalSet.clear()
            goalSet.addAll(goals)
        }
        this
    }

    @Override
    Set<String> getGoals() {
        goalSet
    }

    @Override
    List<String> getCommandLine() {
        prepareCommandLine()
        super.commandLine
    }

    @TypeChecked
    @Override
    MavenExecSpec options(@DelegatesTo(MavenCli) Closure options) {
        this.mavenCli.with options
        this
    }

    private List<String> prepareCommandLine() {
        List<String> parameters = []
        if (windows) {
            parameters << 'cmd'
            parameters << '/c'
        }
        String command = ''
        if (mavenDir != null) {
            command = mavenDir.absolutePath + '/'
        }
        command += 'mvn'
        if (windows) {
            command += '.cmd'
        }
        parameters << command
        parameters.addAll(mavenCli.toCommandLine())
        parameters.addAll(goals)
        super.setCommandLine(parameters)
    }
}
