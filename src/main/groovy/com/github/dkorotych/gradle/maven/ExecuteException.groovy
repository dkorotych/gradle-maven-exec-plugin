/*
 * Copyright 2018 Dmitry Korotych
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
package com.github.dkorotych.gradle.maven

import org.gradle.process.internal.ExecException

/**
 * Exception with information about Maven execution issue.
 */
class ExecuteException extends ExecException {
    private final String commandLine

    /**
     * Create new Maven execution exception.
     *
     * @param message Exception description
     * @param commandLine executed command line
     * @param throwable Real execution exception
     */
    ExecuteException(String message, String commandLine, Throwable throwable) {
        super(message, throwable)
        this.commandLine = commandLine
    }
}
