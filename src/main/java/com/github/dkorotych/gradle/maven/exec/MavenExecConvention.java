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

import groovy.lang.Closure;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.process.ExecResult;

import java.util.*;
import java.util.stream.StreamSupport;

/**
 * Extension for a project to support methods to directly invoke the Maven task execution.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
public class MavenExecConvention {
    private final Project project;

    /**
     * New instance of the Maven convention.
     *
     * @param project Current Gradle project
     */
    public MavenExecConvention(final Project project) {
        this.project = Objects.requireNonNull(project, "Project should be not null");
    }

    /**
     * Execute Maven directly in any task.
     *
     * @param configure Configuration closure for execute action
     * @return Result of execution
     */
    public ExecResult mavenexec(final Closure<MavenExecSpec> configure) {
        Objects.requireNonNull(configure, "Configure closure should not be null");
        try {
            return project.exec(execSpec -> {
                final MavenExecSpecDelegate delegate = new MavenExecSpecDelegate(execSpec, project);
                project.configure(delegate, configure);
                final List<String> commandLine = delegate.getCommandLine();
                execSpec.setCommandLine(commandLine);
                project.getLogger().info("Execute Maven command: {}", String.join(" ", commandLine));
            }).assertNormalExitValue();
        } catch (Exception exception) {
            if (exception.getCause() != null) {
                printCauseMessagesWithoutLast(exception, project.getLogger());
            }
            throw exception;
        }
    }

    @SuppressWarnings("common-java:DuplicatedBlocks")
    private void printCauseMessagesWithoutLast(final Exception e, final Logger logger) {
        final Iterator<Throwable> iterator = new Iterator<>() {
            private Throwable current = e;

            @Override
            public boolean hasNext() {
                return current.getCause() != null;
            }

            @Override
            public Throwable next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                current = current.getCause();
                return current;
            }
        };
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator,
                        Spliterator.ORDERED | Spliterator.IMMUTABLE), false)
                .filter(exception -> (exception.getCause()) != null)
                .map(Throwable::getMessage)
                .filter(StringUtils::isNotBlank)
                .forEachOrdered(logger::warn);
    }
}
