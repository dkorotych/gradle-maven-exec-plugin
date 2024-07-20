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

import org.gradle.api.Project;
import org.gradle.api.internal.project.DefaultProject;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.process.ExecSpec;
import org.gradle.process.internal.ExecActionFactory;
import org.junit.platform.commons.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.gradle.internal.os.OperatingSystem.*;

@SuppressWarnings("MissingJavadocType")
public final class TestUtility {
    private TestUtility() {
    }

    public static ExecSpec createExecSpec(Project project) {
        return Optional.ofNullable(project)
                .filter(DefaultProject.class::isInstance)
                .map(DefaultProject.class::cast)
                .map(DefaultProject::getServices)
                .map(registry -> registry.get(ExecActionFactory.class))
                .map(ExecActionFactory::newExecAction)
                .orElseThrow(RuntimeException::new);
    }

    public static void prepareProject(boolean withWrapper, File destination) throws Exception {
        final String directory = "/fixtures/wrapper/with" + (withWrapper ? "" : "out");
        final URI uri = requireNonNull(TestUtility.class.getResource(directory)).toURI();
        final File source = Paths.get(uri).toFile();
        FileUtils.copyDirectory(source, destination);
        if (withWrapper) {
            final Path path = destination.toPath();
            for (String name : Arrays.asList("mvnw", "mvnw.cmd")) {
                path.resolve(name).toFile().setExecutable(true);
            }
        }
    }

    public static List<OperatingSystem> operatingSystems() {
        return Arrays.asList(FREE_BSD, LINUX, MAC_OS, SOLARIS, WINDOWS);
    }

    public static void setOperatingSystem(OperatingSystem os) {
        if (OperatingSystem.current().equals(os)) {
            return;
        }
        final String name;
        if (os.isWindows()) {
            name = "windows";
        } else {
            if (os.isMacOsX()) {
                name = "mac os x";
            } else {
                if (os.isLinux()) {
                    name = "linux";
                } else {
                    if (SOLARIS == os) {
                        name = "solaris";
                    } else {
                        name = "freebsd";
                    }
                }
            }
        }
        final String key = "os.name";
        final String property = System.getProperty(key);
        synchronized (OperatingSystem.class) {
            resetCurrentOperatingSystem();
            System.setProperty(key, name);
            OperatingSystem.current();
            System.setProperty(key, property);
        }
    }

    public static List<String> commandLine(File path, String... arguments) {
        return commandLine(path, OperatingSystem.current(), false, true, arguments);
    }

    public static List<String> commandLine(final File path, OperatingSystem os, boolean oldVersion, boolean useWrapper,
                                           String... arguments) {
        final List<String> commandLine = new ArrayList<>();
        File pathToMaven = path;
        if (path != null && !useWrapper) {
            pathToMaven = path.toPath()
                    .resolve("bin")
                    .toFile();
        }
        final String mavenHome = Optional.ofNullable(pathToMaven)
                .map(File::toPath)
                .map(Path::normalize)
                .map(Path::toFile)
                .map(File::getAbsolutePath)
                .map(absolutePath -> absolutePath + File.separatorChar)
                .orElse("");
        commandLine.add(mavenHome + "mvn"
                + (useWrapper ? 'w' : "") + (os.isWindows() ? useWrapper ? ".cmd" : oldVersion ? ".bat" : ".cmd" : ""));
        commandLine.addAll(Arrays.asList(arguments));
        return commandLine;
    }

    public static void resetCurrentOperatingSystem() {
        try {
            final Method method = ReflectionUtils.getRequiredMethod(OperatingSystem.class, "resetCurrent");
            method.setAccessible(true);
            method.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
