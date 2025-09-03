/**
 * Copyright 2025 Dmitry Korotych
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

import org.gradle.api.Project;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.gradle.process.ExecOperations;

import javax.inject.Inject;
import java.util.Objects;

import static com.github.dkorotych.gradle.maven.exec.MavenExecPlugin.OPERATION_NAME;

/**
 * Interface for obtaining an instance of {@link ExecOperations}, used to perform external operations.
 * Provides a method to get the {@code ExecOperations} object via injection or from the project's extra properties.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
public interface ExecOperationsInstanceHolder {

    /**
     * An {@code @Inject}-annotated method that returns an instance of {@link ExecOperations}.
     * Used by Gradle for dependency injection.
     *
     * @return An instance of {@code ExecOperations}
     */
    @Inject
    ExecOperations getExecOperations();

    /**
     * A static method to retrieve {@link ExecOperations} from the project.
     * Checks if the object is present in the project's extra properties. If it is not,
     * a new instance of {@code ExecOperations} is created using a factory method.
     *
     * @param project The Gradle project object
     * @return An instance of {@code ExecOperations}
     */
    static ExecOperations getExecOperations(final Project project) {
        final ExtraPropertiesExtension properties = project.getExtensions().getExtraProperties();
        if (properties.has(OPERATION_NAME)) {
            return (ExecOperations) properties.get(OPERATION_NAME);
        } else {
            return Objects.requireNonNull(project.getObjects()
                    .newInstance(ExecOperationsInstanceHolder.class)
                    .getExecOperations());
        }
    }
}
