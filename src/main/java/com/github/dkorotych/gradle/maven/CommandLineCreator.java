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

/**
 * A builder that allows you to build a Maven execution command and supported options depending on the operating
 * system and Maven installation directory.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com).
 */
public final class CommandLineCreator {
    private final MavenExecSpec specification;
    private final Project project;
    private final Logger logger;
    private final MavenHomeProvider mavenHomeProvider;

    private String executable;
    private List<String> arguments;

    /**
     * Create Maven command line builder.
     *
     * @param specification Options for launching a Maven process.
     * @param project       Current project
     */
    public CommandLineCreator(final MavenExecSpec specification, final Project project) {
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

    /**
     * Prepare Maven process executable for entered specifications.
     *
     * @return Maven process executable
     * @see MavenDescriptor#getExecutable()
     */
    public String getExecutable() {
        return executable;
    }

    /**
     * Prepare Maven process arguments for entered specifications.
     *
     * @return Command line arguments
     * @see MavenOptionsToCommandLineAdapter#asCommandLine()
     */
    public List<String> getArguments() {
        return arguments;
    }

    private void prepareCommandLine() {
        if (getMavenDir() == null) {
            logger.info("""
                    The directory with the executable Maven file is not set. \
                    The plugin will try to find the installation by itself\
                    """);
            findMavenExecutable();
            if (getMavenDir() == null) {
                throw new GradleException("Maven installation not found");
            } else {
                logger.info("Use Maven from: {}", getMavenDir());
            }
        }
        final Path mavenHome = Optional.ofNullable(getMavenDir())
                .map(File::toPath)
                .orElse(null);
        final File workingDir = specification.getWorkingDir();
        final MavenDescriptor descriptor = new MavenDescriptor(mavenHome, workingDir, project);
        executable = descriptor.getExecutable();
        arguments = new ArrayList<>(new MavenOptionsToCommandLineAdapter(specification.getOptions(),
                descriptor.getSupportedOptions()).asCommandLine());
        arguments.addAll(specification.getGoals());
    }

    private void findMavenExecutable() {
        findMavenInDirectory(specification.getWorkingDir());
        if (getMavenDir() == null && !mavenHomeProvider.findMavenHome()) {
            findMavenInDirectory(project.getLayout().getBuildDirectory().getAsFile().get());
            findMavenInDirectory(project.getProjectDir());
            findMavenInDirectory(project.getRootDir());
        }
    }

    private void findMavenInDirectory(final File directory) {
        if (getMavenDir() == null) {
            logger.info("Find Maven executable in {}", directory);
            try {
                mavenHomeProvider.setMavenHome(directory);
                specification.setMavenDir(directory);
            } catch (IncorrectMavenInstallationDirectoryException ignored) {
                logger.debug("Maven executable not found {}", directory);
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
