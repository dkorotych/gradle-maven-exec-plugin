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

import org.gradle.api.GradleException;

import java.io.File;
import java.util.Objects;

/**
 * Exception with information about Maven installation or finding issue.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
public class IncorrectMavenInstallationDirectoryException extends GradleException {
    /**
     * Create a Maven installation exception.
     *
     * @param directory Maven installation directory
     */
    public IncorrectMavenInstallationDirectoryException(final File directory) {
        super("Maven home is set to: '"
                + Objects.toString(directory, "blank")
                + "' which is not a correct Maven installation directory");
    }
}
