package com.github.dkorotych.gradle.maven.exec

import com.github.dkorotych.gradle.maven.cli.MavenCli
import groovy.transform.Memoized
import groovy.transform.TypeChecked
import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.os.OperatingSystem

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class MavenExec extends AbstractExecTask<MavenExec> implements MavenExecSpec {
    private final boolean windows
    private final MavenCli mavenCli = new MavenCli()
    private final Set<String> goalSet = []
    private File installDir

    MavenExec() {
        super(MavenExec)
        windows = OperatingSystem.current().isWindows()
    }

    @Override
    @TaskAction
    protected void exec() {
        // build correct command line
        List<String> line = commandLine
        logger.debug('command line {}', line)
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
        List<String> parameters = []
        parameters.addAll(executableCommandLine())
        parameters.addAll(mavenCli.toCommandLine())
        parameters.addAll(goals)
        super.setCommandLine(parameters)
        super.commandLine
    }

    @TypeChecked
    @Override
    MavenExecSpec options(@DelegatesTo(MavenCli) Closure options) {
        this.mavenCli.with options
        this
    }

    @Memoized
    private GString command() {
        "${mavenDir ? mavenDir.absolutePath + '/' : ''}mvn${windows ? '.cmd' : ''}"
    }

    @Memoized
    private List<String> executableCommandLine() {
        List<String> commandLine = []
        if (windows) {
            commandLine << 'cmd'
            commandLine << '/c'
        }
        commandLine << command()
        commandLine
    }
}
