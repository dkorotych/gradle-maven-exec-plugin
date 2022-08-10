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
package com.github.dkorotych.gradle.maven.exec.test;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.internal.os.OperatingSystem;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("MissingJavadocType")
public abstract class MavenDependentTask extends DefaultTask {
    protected String version;

    public void setVersion(final String version) {
        this.version = version;
        final Task task = requireNonNull(getProject().getTasks().findByName(PrepareMavenTask.createName(version)));
        getDependsOn().add(task);
    }

    @OutputDirectory
    protected Path getMavenHome() {
        return Optional.of(getProject())
                .map(Project::getExtensions)
                .map(ExtensionContainer::getExtraProperties)
                .map(properties -> properties.get("maven-" + version))
                .filter(File.class::isInstance)
                .map(File.class::cast)
                .map(File::toPath)
                .orElse(null);
    }

    @Internal
    protected String getMavenExecutable() {
        return Optional.ofNullable(getMavenHome())
                .map(path -> path.resolve("mvnw" + (OperatingSystem.current().isWindows() ? ".cmd" : "")))
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .orElse(null);
    }
}
