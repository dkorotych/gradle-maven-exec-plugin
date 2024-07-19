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

import org.apache.commons.lang3.StringUtils;
import org.gradle.api.GradleException;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.beans.Introspector.getBeanInfo;
import static java.util.Arrays.stream;

/**
 * A converter that converts Maven process configuration and invocation options into version-specific command
 * line arguments.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
public final class MavenOptionsToCommandLineAdapter {
    private static final Pattern OPTION_PATTERN = Pattern.compile("(\\p{Upper})");
    private final MavenOptions options;
    private final List<PropertyDescriptor> descriptors;

    /**
     * Create options to command line converter.
     *
     * @param options          Maven options
     * @param supportedOptions Supported options for this Maven version
     */
    public MavenOptionsToCommandLineAdapter(final MavenOptions options, final Set<String> supportedOptions) {
        this.options = Objects.requireNonNull(options, "Maven options should be not null");
        try {
            final Predicate<PropertyDescriptor> predicate = supportedOptions == null
                    ? descriptor -> true
                    : descriptor -> {
                final String option = createOption(descriptor.getName());
                return supportedOptions.contains(option);
            };
            descriptors = stream(getBeanInfo(DefaultMavenOptions.class, Object.class).getPropertyDescriptors())
                    .filter(predicate)
                    .sorted(Comparator.comparing(PropertyDescriptor::getName))
                    .collect(Collectors.toList());
        } catch (IntrospectionException e) {
            throw new GradleException("Can't create property descriptions", e);
        }
    }

    /**
     * Build command line.
     *
     * @return Command line
     */
    @SuppressWarnings({"PMD.CognitiveComplexity", "java:S3776"})
    public List<String> asCommandLine() {
        final ArrayList<String> arguments = new ArrayList<>();
        for (final PropertyDescriptor descriptor : descriptors) {
            final Method readMethod = descriptor.getReadMethod();
            final Class<?> type = descriptor.getPropertyType();
            if (type == boolean.class) {
                addBooleanOption(descriptor, readMethod, arguments);
            } else {
                if (type == String.class) {
                    addStringOption(descriptor, readMethod, arguments);
                } else {
                    if (type == File.class) {
                        addFileOption(descriptor, readMethod, arguments);
                    } else {
                        if (type == String[].class) {
                            addStringArrayOption(descriptor, readMethod, arguments);
                        } else {
                            if (type == Map.class) {
                                // only define
                                addMapOption(options.getDefine(), arguments);
                            } else {
                                throw new GradleException("Unsupported option type - " + type);
                            }
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(arguments);
    }

    private String createOption(final String property) {
        return "--" + OPTION_PATTERN.matcher(property)
                .replaceAll("-$1")
                .toLowerCase(Locale.ENGLISH);
    }

    private void addBooleanOption(final PropertyDescriptor descriptor, final Method readMethod,
                                  final List<String> arguments) {
        try {
            if (Boolean.TRUE.equals(readMethod.invoke(options))) {
                arguments.add(createOption(descriptor.getName()));
            }
        } catch (Exception e) {
            throw new GradleException("Can't convert boolean property to option", e);
        }
    }

    private void addStringOption(final PropertyDescriptor descriptor, final Method readMethod,
                                 final List<String> arguments) {
        try {
            final String value = (String) readMethod.invoke(options);
            if (StringUtils.isNotBlank(value)) {
                addStringOption(descriptor.getName(), value, arguments);
            }
        } catch (Exception e) {
            throw new GradleException("Can't convert string property to option", e);
        }
    }

    private void addStringOption(final String property, final String value, final List<String> arguments) {
        if (StringUtils.isNotBlank(value)) {
            arguments.add(createOption(property));
            arguments.add(doubleQuoteIfNecessary(value));
        }
    }

    private void addFileOption(final PropertyDescriptor descriptor, final Method readMethod,
                               final List<String> arguments) {
        try {
            final File value = (File) readMethod.invoke(options);
            if (value != null) {
                addStringOption(descriptor.getName(), value.getAbsolutePath(), arguments);
            }
        } catch (Exception e) {
            throw new GradleException("Can't convert file property to option", e);
        }
    }

    private void addStringArrayOption(final PropertyDescriptor descriptor, final Method readMethod,
                                      final List<String> arguments) {
        try {
            final String[] value = (String[]) readMethod.invoke(options);
            if (value != null && value.length > 0) {
                final String arrayAsString = stream(value)
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.joining(","));
                addStringOption(descriptor.getName(), arrayAsString, arguments);
            }
        } catch (Exception e) {
            throw new GradleException("Can't convert string array property to option", e);
        }
    }

    private void addMapOption(final Map<String, String> value, final List<String> arguments) {
        if (value != null) {
            value.entrySet().stream()
                    .filter(entry -> StringUtils.isNotBlank(entry.getKey()) && StringUtils.isNotBlank(entry.getValue()))
                    .map(entry -> "-D" + entry.getKey() + '=' + doubleQuoteIfNecessary(entry.getValue()))
                    .forEach(arguments::add);
        }
    }

    private String doubleQuoteIfNecessary(final String value) {
        if (StringUtils.containsWhitespace(value) && !value.startsWith("\"")) {
            return '"' + value + '"';
        }
        return value;
    }
}
