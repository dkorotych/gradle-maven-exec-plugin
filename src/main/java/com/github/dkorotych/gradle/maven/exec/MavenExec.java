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

import com.github.dkorotych.gradle.maven.CommandLineCreator;
import com.github.dkorotych.gradle.maven.DefaultMavenOptions;
import com.github.dkorotych.gradle.maven.MavenOptions;
import org.gradle.api.tasks.AbstractExecTask;
import org.gradle.process.ProcessForkOptions;

import javax.annotation.Nullable;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the task to support the launch Maven.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
public class MavenExec extends AbstractExecTask<MavenExec> implements MavenExecSpec {
    private final MavenOptions options = new DefaultMavenOptions();
    private final Set<String> goals = new LinkedHashSet<>();
    private File mavenDir;

    /**
     * Create implementation of the task.
     */
    public MavenExec() {
        super(MavenExec.class);
        setWorkingDir(getProject().getProjectDir());
    }

    @Override
    public void exec() {
        final CommandLineCreator commandLineCreator = new CommandLineCreator(this, getProject());
        super.setExecutable(commandLineCreator.getExecutable());
        super.setArgs(commandLineCreator.getArguments());
        super.exec();
    }

    @Override
    public File getMavenDir() {
        return mavenDir;
    }

    @Override
    public void setMavenDir(final File dir) {
        mavenDir = dir;
    }

    @Override
    public Set<String> getGoals() {
        return goals;
    }

    @Override
    public MavenOptions getOptions() {
        return options;
    }

    @Override
    public MavenExec commandLine(final Object... arguments) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MavenExec commandLine(final Iterable<?> args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MavenExec args(final Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MavenExec args(final Iterable<?> args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MavenExec setArgs(final List<String> arguments) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MavenExec setArgs(final @Nullable Iterable<?> arguments) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCommandLine(final List<String> args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCommandLine(final Iterable<?> args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCommandLine(final Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExecutable(final @Nullable String executable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExecutable(final Object executable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MavenExec executable(final Object executable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MavenExec copyTo(final ProcessForkOptions target) {
        throw new UnsupportedOperationException();
    }
}
