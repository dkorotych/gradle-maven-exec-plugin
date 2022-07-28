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
import java.util.*;

import static org.gradle.internal.os.OperatingSystem.*;

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
        final URI uri = Objects.requireNonNull(TestUtility.class.getResource("/fixtures/wrapper/with" + (withWrapper ? "" : "out"))).toURI();
        final File source = Paths.get(uri).toFile();
        FileUtils.copyDirectory(source, destination);
        if (withWrapper) {
            Path path = destination.toPath();
            for (String name : Arrays.asList("mvnw", "mvnw.cmd")) {
                path.resolve(name).toFile().setExecutable(true);
            }
        }
    }

    public static List<OperatingSystem> operatingSystems() {
        return Arrays.asList(FREE_BSD, LINUX, MAC_OS, SOLARIS, WINDOWS);
    }

    public static void setOperatingSystem(OperatingSystem os) {
        if (WINDOWS == os) {
            asWindows();
        } else {
            asUnix();
        }
    }

    public static void asWindows() {
        resetCurrentOperatingSystem();
        System.setProperty("os.name", "windows");
    }

    public static void asUnix() {
        resetCurrentOperatingSystem();
        System.setProperty("os.name", "linux");
    }

    public static List<String> commandLine(File path, String... arguments) {
        return commandLine(path, OperatingSystem.current(), false, true, arguments);
    }

    public static List<String> commandLine(File path, OperatingSystem os, boolean oldVersion, boolean useWrapper, String... arguments) {
        List<String> commandLine = new ArrayList<>();
        if (os == WINDOWS) {
            commandLine.add("cmd");
            commandLine.add("/c");
        }
        if (path != null) {
            if (!useWrapper) {
                path = path.toPath().resolve("bin").toFile();
            }

        }
        commandLine.add((path != null ? path.getAbsolutePath() + File.separatorChar : "") + "mvn"
                + (useWrapper ? 'w' : "") + (os == WINDOWS ? useWrapper ? ".cmd" : oldVersion ? ".bat" : ".cmd" : ""));
        commandLine.addAll(Arrays.asList(arguments));
        return commandLine;
    }

    private static void resetCurrentOperatingSystem() {
        try {
            final Method method = ReflectionUtils.getRequiredMethod(OperatingSystem.class, "resetCurrent");
            method.setAccessible(true);
            method.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
