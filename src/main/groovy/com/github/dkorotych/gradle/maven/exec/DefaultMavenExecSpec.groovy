package com.github.dkorotych.gradle.maven.exec

import com.github.dkorotych.gradle.maven.cli.MavenCli
import groovy.transform.PackageScope
import groovy.transform.TypeChecked
import org.gradle.internal.os.OperatingSystem

/**
 * Default implementation for specifies options for launching a Maven process.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@PackageScope
trait DefaultMavenExecSpec implements MavenExecSpec {
    @Delegate(
            excludes = [
                    'addPropertyChangeListener',
                    'removePropertyChangeListener',
                    'firePropertyChange',
                    'getPropertyChangeListeners',
            ],
            interfaces = false)
    private final MavenCli mavenCli = new MavenCli()
    private final Set<String> goalSet = []
    private final boolean windows = OperatingSystem.current().isWindows()
    private File installDir

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
    }

    @TypeChecked
    @Override
    MavenExecSpec options(@DelegatesTo(MavenCli) Closure options) {
        this.mavenCli.with options
        this
    }

    @Override
    void setExecutable(Object executable) {
        throw new UnsupportedOperationException()
    }

    @Override
    MavenExec executable(Object executable) {
        throw new UnsupportedOperationException()
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
        parameters
    }
}
