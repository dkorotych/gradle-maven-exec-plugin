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
import com.github.dkorotych.gradle.maven.MavenOptions;
import groovy.lang.Closure;
import org.gradle.api.Project;
import org.gradle.process.ExecSpec;
import org.gradle.process.ProcessForkOptions;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Specifies options for launching a Maven process.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
public class MavenExecSpecDelegate implements MavenExecSpec {
    private final MavenProperties properties = new MavenProperties();
    private final ExecSpec delegate;
    private final Project project;

    /**
     * Create new options for launching a Maven process.
     *
     * @param delegate Real process executor
     * @param project  Current project
     */
    public MavenExecSpecDelegate(final ExecSpec delegate, final Project project) {
        this.delegate = Objects.requireNonNull(delegate, "Exec specification delegate should be not null");
        this.project = Objects.requireNonNull(project, "Project should be not null");
    }

    @Override
    @SuppressWarnings("common-java:DuplicatedBlocks")
    public File getMavenDir() {
        return properties.getMavenDir();
    }

    @Override
    @SuppressWarnings("common-java:DuplicatedBlocks")
    public void setMavenDir(final File dir) {
        properties.setMavenDir(dir);
    }

    @Override
    @SuppressWarnings("common-java:DuplicatedBlocks")
    public Set<String> getGoals() {
        return properties.getGoals();
    }

    @Override
    public MavenExecSpec options(final Closure<MavenOptions> options) {
        project.configure(getOptions(), options);
        return this;
    }

    @Override
    @SuppressWarnings("common-java:DuplicatedBlocks")
    public MavenOptions getOptions() {
        return properties.getOptions();
    }

    @Override
    public MavenExecSpec setIgnoreExitValue(final boolean ignoreExitValue) {
        delegate.setIgnoreExitValue(ignoreExitValue);
        return this;
    }

    @Override
    public boolean isIgnoreExitValue() {
        return delegate.isIgnoreExitValue();
    }

    @Override
    public MavenExecSpec setStandardInput(final InputStream inputStream) {
        delegate.setStandardInput(inputStream);
        return this;
    }

    @Override
    public InputStream getStandardInput() {
        return delegate.getStandardInput();
    }

    @Override
    public MavenExecSpec setStandardOutput(final OutputStream outputStream) {
        delegate.setStandardOutput(outputStream);
        return this;
    }

    @Override
    public OutputStream getStandardOutput() {
        return delegate.getStandardOutput();
    }

    @Override
    public MavenExecSpec setErrorOutput(final OutputStream outputStream) {
        delegate.setErrorOutput(outputStream);
        return this;
    }

    @Override
    public OutputStream getErrorOutput() {
        return delegate.getErrorOutput();
    }

    @Override
    public List<String> getCommandLine() {
        getExecutable();
        return delegate.getCommandLine();
    }

    @Override
    public String getExecutable() {
        final CommandLineCreator creator = new CommandLineCreator(this, project);
        delegate.setExecutable(creator.getExecutable());
        delegate.setArgs(creator.getArguments());
        return delegate.getExecutable();
    }

    @Override
    public void setExecutable(final String executable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExecutable(final Object executable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MavenExecSpec executable(final Object executable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public File getWorkingDir() {
        return delegate.getWorkingDir();
    }

    @Override
    public void setWorkingDir(final File dir) {
        delegate.setWorkingDir(dir);
    }

    @Override
    public void setWorkingDir(final Object dir) {
        delegate.setWorkingDir(dir);
    }

    @Override
    public MavenExecSpec workingDir(final Object dir) {
        delegate.setWorkingDir(dir);
        return this;
    }

    @Override
    public Map<String, Object> getEnvironment() {
        return delegate.getEnvironment();
    }

    @Override
    public void setEnvironment(final Map<String, ?> environmentVariables) {
        if (environmentVariables == null) {
            delegate.getEnvironment().clear();
        } else {
            delegate.setEnvironment(environmentVariables);
        }
    }

    @Override
    public MavenExecSpec environment(final Map<String, ?> environmentVariables) {
        setEnvironment(environmentVariables);
        return this;
    }

    @Override
    public MavenExecSpec environment(final String name, final Object value) {
        delegate.environment(name, value);
        return this;
    }

    @Override
    public MavenExecSpec copyTo(final ProcessForkOptions options) {
        throw new UnsupportedOperationException();
    }
}
