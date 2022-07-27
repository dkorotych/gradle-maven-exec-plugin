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
package com.github.dkorotych.gradle.maven;

import com.github.dkorotych.gradle.maven.exec.MavenExecSpec;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CommandLineCreator {
    private final MavenExecSpec specification;
    private final Project project;
    private final Logger logger;
    private final MavenHomeProvider mavenHomeProvider;

    private String executable;
    private List<String> arguments;

    public CommandLineCreator(MavenExecSpec specification, Project project) {
        this.specification = Objects.requireNonNull(specification, "Specification should be not null");
        this.project = Objects.requireNonNull(project, "Project should be not null");
        logger = project.getLogger();
        mavenHomeProvider = new MavenHomeProvider();
        final File mavenDir = specification.getMavenDir();
        if (mavenDir != null) {
            mavenHomeProvider.setMavenHome(mavenDir);
        }
        prepareCommandLine();
    }

    public String getExecutable() {
        return executable;
    }

    public List<String> getArguments() {
        return arguments;
    }

    private void prepareCommandLine() {
        if (getMavenDir() == null) {
            logger.info("The directory with the executable Maven file is not set. The plugin will try to find the installation by itself");
            findMavenExecutable();
            if (getMavenDir() == null) {
                throw new GradleException("Maven installation not found");
            } else {
                logger.info("Use Maven from: {}", getMavenDir());
            }
        }
        final MavenDescriptor descriptor = new MavenDescriptor(getMavenDir().toPath(), project);
        executable = descriptor.getExecutable();
        arguments = new ArrayList<>(new MavenOptionsToCommandLineAdapter(specification.getOptions(),
                descriptor.getSupportedOptions()).asCommandLine());
        arguments.addAll(specification.getGoals());
    }

    private void findMavenExecutable() {
        findMavenInDirectory(specification.getWorkingDir());
        if (getMavenDir() == null) {
            if (!mavenHomeProvider.findMavenHome()) {
                findMavenInDirectory(project.getBuildDir());
                findMavenInDirectory(project.getProjectDir());
                findMavenInDirectory(project.getRootDir());
            }
        }
    }

    private void findMavenInDirectory(File directory) {
        if (getMavenDir() == null) {
            logger.info("Find Maven executable in {}", directory);
            try {
                mavenHomeProvider.setMavenHome(directory);
            } catch (IncorrectMavenInstallationDirectoryException ignored) {
                logger.info("Maven executable not found {}", directory);
            }
        }
    }

    private File getMavenDir() {
        return Optional.of(mavenHomeProvider)
                .map(MavenHomeProvider::getMavenHome)
                .map(Path::toFile)
                .orElse(null);
    }
}
