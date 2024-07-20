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

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.code.beanmatchers.BeanMatchers.*;
import static com.google.common.collect.ImmutableMap.of;
import static java.beans.Introspector.getBeanInfo;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("OverloadMethodsDeclarationOrder")
class DefaultMavenOptionsTest {
    public static Stream<PropertyDescriptor> lookLikeAPropertyAccess() throws IntrospectionException {
        return stream(getBeanInfo(DefaultMavenOptions.class, Object.class).getPropertyDescriptors());
    }

    @Test
    void asBean() {
        assertThat(DefaultMavenOptions.class)
                .satisfies(
                        arg -> assertThat(arg, hasValidBeanConstructor()),
                        arg -> assertThat(arg, hasValidGettersAndSetters()),
                        arg -> assertThat(arg, hasValidBeanHashCode()),
                        arg -> assertThat(arg, hasValidBeanEquals()),
                        arg -> assertThat(arg, hasValidBeanToString())
                );
    }

    @Test
    void allSupportedOptions() throws Exception {
        final URL resource = DefaultMavenOptionsTest.class.getResource("/fixtures/descriptor");
        final Path dir = Paths.get(requireNonNull(resource).toURI());
        final Set<String> allOptions = new HashSet<>();
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(dir, entry -> entry.toFile().isDirectory())) {
            for (Path path : paths) {
                try (Stream<String> lines = Files.lines(path.resolve("options.txt"), UTF_8)) {
                    lines.forEach(allOptions::add);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        lookLikeAPropertyAccess()
                .map(MavenOptionsToCommandLineAdapterTest::createOptionName)
                .forEach(option -> {
                    final boolean remove = allOptions.remove(option);
                    if (!remove) {
                        fail("Option '" + option + "' exists but this is unsupported options");
                    }
                });
        if (!allOptions.isEmpty()) {
            fail(allOptions + " found in option files but not found in class");
        }
    }

    @ParameterizedTest
    @MethodSource
    void lookLikeAPropertyAccess(PropertyDescriptor descriptor) throws Exception {
        final DefaultMavenOptions options = new DefaultMavenOptions();
        assertThatNoException().isThrownBy(descriptor::getReadMethod);
        assertThatNoException().isThrownBy(() -> getPropertyReader(descriptor));
        assertThatNoException().isThrownBy(descriptor::getWriteMethod);
        assertThatNoException().isThrownBy(() -> getPropertyWriter(descriptor));

        switch (descriptor.getPropertyType().getSimpleName()) {
            case "boolean" -> {
                for (boolean value : asList(true, false)) {
                    resetToDefault(options, descriptor);
                    validateSetter(descriptor, options, value);
                    resetToDefault(options, descriptor);
                    validatePropertySetter(descriptor, options, value);
                }
            }
            case "String" -> {
                resetToDefault(options, descriptor);
                validateSetter(descriptor, options, random(10));
                resetToDefault(options, descriptor);
                validatePropertySetter(descriptor, options, random(10));
            }
            case "File" -> {
                resetToDefault(options, descriptor);
                validateSetter(descriptor, options, SystemUtils.getJavaIoTmpDir());
                resetToDefault(options, descriptor);
                validatePropertySetter(descriptor, options, SystemUtils.getUserHome());
            }
            case "String[]" -> {
                resetToDefault(options, descriptor);
                validateSetter(descriptor, options, new String[]{random(10), random(10), random(10)});
                resetToDefault(options, descriptor);
                validatePropertySetter(descriptor, options, new String[]{random(10), random(10), random(10)});
            }
            case "Map" -> {
                resetToDefault(options, descriptor);
                validateSetter(descriptor, options, of(random(10), random(10), random(5), random(8)));
                resetToDefault(options, descriptor);
                validatePropertySetter(descriptor, options, of(random(10), random(10), random(5), random(8)));
            }
            default -> fail("Unsupported property type - %s", descriptor.getPropertyType());
        }
    }

    private Method getPropertyReader(PropertyDescriptor descriptor) throws NoSuchMethodException {
        return DefaultMavenOptions.class.getMethod(descriptor.getName());
    }

    private Method getPropertyWriter(PropertyDescriptor descriptor) throws NoSuchMethodException {
        return DefaultMavenOptions.class.getMethod(descriptor.getName(), descriptor.getPropertyType());
    }

    private void resetToDefault(DefaultMavenOptions options, PropertyDescriptor descriptor) throws Exception {
        final Object value;
        if (descriptor.getPropertyType() != boolean.class) {
            value = null;
        } else {
            value = false;
        }
        getPropertyWriter(descriptor).invoke(options, value);
        descriptor.getWriteMethod().invoke(options, value);
    }

    private void validateSetter(PropertyDescriptor descriptor, DefaultMavenOptions options, Object value)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        descriptor.getWriteMethod().invoke(options, value);
        assertThat(descriptor.getReadMethod().invoke(options))
                .isEqualTo(value);
        assertThat(getPropertyReader(descriptor).invoke(options))
                .isEqualTo(value);
    }

    private void validatePropertySetter(PropertyDescriptor descriptor, DefaultMavenOptions options, Object value)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        getPropertyWriter(descriptor).invoke(options, value);
        assertThat(getPropertyReader(descriptor).invoke(options))
                .isEqualTo(value);
        assertThat(descriptor.getReadMethod().invoke(options))
                .isEqualTo(value);
    }
}
