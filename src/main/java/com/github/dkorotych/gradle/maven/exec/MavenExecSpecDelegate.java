/**
 * Copyright 2022 Dmitry Korotych
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
package com.github.dkorotych.gradle.maven.exec;

import com.github.dkorotych.gradle.maven.CommandLineCreator;
import com.github.dkorotych.gradle.maven.DefaultMavenOptions;
import com.github.dkorotych.gradle.maven.MavenOptions;
import org.gradle.api.Project;
import org.gradle.process.ExecSpec;
import org.gradle.process.ProcessForkOptions;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class MavenExecSpecDelegate implements MavenExecSpec {
    private File mavenDir;
    private final MavenOptions options = new DefaultMavenOptions();
    private final Set<String> goals = new LinkedHashSet<>();
    private final ExecSpec delegate;
    private final Project project;

    public MavenExecSpecDelegate(ExecSpec delegate, Project project) {
        this.delegate = Objects.requireNonNull(delegate, "Exec specification delegate should be not null");
        this.project = Objects.requireNonNull(project, "Project should be not null");
    }

    @Override
    public File getMavenDir() {
        return mavenDir;
    }

    @Override
    public void setMavenDir(File dir) {
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
    public MavenExecSpec setIgnoreExitValue(boolean ignoreExitValue) {
        delegate.setIgnoreExitValue(ignoreExitValue);
        return this;
    }

    @Override
    public boolean isIgnoreExitValue() {
        return delegate.isIgnoreExitValue();
    }

    @Override
    public MavenExecSpec setStandardInput(InputStream inputStream) {
        delegate.setStandardInput(inputStream);
        return this;
    }

    @Override
    public InputStream getStandardInput() {
        return delegate.getStandardInput();
    }

    @Override
    public MavenExecSpec setStandardOutput(OutputStream outputStream) {
        delegate.setStandardOutput(outputStream);
        return this;
    }

    @Override
    public OutputStream getStandardOutput() {
        return delegate.getStandardOutput();
    }

    @Override
    public MavenExecSpec setErrorOutput(OutputStream outputStream) {
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
    public void setExecutable(String executable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExecutable(Object executable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MavenExecSpec executable(Object executable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public File getWorkingDir() {
        return delegate.getWorkingDir();
    }

    @Override
    public void setWorkingDir(File dir) {
        delegate.setWorkingDir(dir);
    }

    @Override
    public void setWorkingDir(Object dir) {
        delegate.setWorkingDir(dir);
    }

    @Override
    public MavenExecSpec workingDir(Object dir) {
        delegate.setWorkingDir(dir);
        return this;
    }

    @Override
    public Map<String, Object> getEnvironment() {
        return delegate.getEnvironment();
    }

    @Override
    public void setEnvironment(Map<String, ?> environmentVariables) {
        if (environmentVariables == null) {
            delegate.getEnvironment().clear();
        } else {
            delegate.setEnvironment(environmentVariables);
        }
    }

    @Override
    public MavenExecSpec environment(Map<String, ?> environmentVariables) {
        setEnvironment(environmentVariables);
        return this;
    }

    @Override
    public MavenExecSpec environment(String name, Object value) {
        delegate.environment(name, value);
        return this;
    }

    @Override
    public MavenExecSpec copyTo(ProcessForkOptions options) {
        throw new UnsupportedOperationException();
    }
}
