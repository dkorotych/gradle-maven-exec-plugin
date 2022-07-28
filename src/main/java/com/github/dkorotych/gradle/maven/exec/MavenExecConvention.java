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

import groovy.lang.Closure;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.process.ExecResult;
import org.gradle.util.ClosureBackedAction;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class MavenExecConvention {
    private final Project project;

    public MavenExecConvention(Project project) {
        this.project = Objects.requireNonNull(project, "Project should be not null");
    }

    /**
     * Execute Maven directly in any task.
     *
     * @param configure Configuration closure for execute action
     * @return Result of execution
     */
    public ExecResult mavenexec(Closure<MavenExecSpec> configure) {
        Objects.requireNonNull(configure, "Configure closure should not be null");
        try {
            return project.exec(execSpec -> {
                final MavenExecSpecDelegate delegate = new MavenExecSpecDelegate(execSpec, project);
                ClosureBackedAction.execute(delegate, configure);
                execSpec.setExecutable(delegate.getExecutable());
            }).assertNormalExitValue();
        } catch (Throwable throwable) {
            if (throwable.getCause() != null) {
                printCauseMessagesWithoutLast(throwable, project.getLogger());
            }
            throw throwable;
        }
    }

    private void printCauseMessagesWithoutLast(Throwable throwable, Logger logger) {
        final Iterator<Throwable> iterator = new Iterator<Throwable>() {
            private Throwable current = throwable;

            @Override
            public boolean hasNext() {
                return current.getCause() != null;
            }

            @Override
            public Throwable next() {
                return current = current.getCause();
            }
        };
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator,
                Spliterator.ORDERED | Spliterator.IMMUTABLE), false)
                .filter(exception -> Objects.nonNull(exception.getCause()))
                .map(Throwable::getMessage)
                .filter(StringUtils::isNotBlank)
                .forEachOrdered(logger::warn);
    }
}
