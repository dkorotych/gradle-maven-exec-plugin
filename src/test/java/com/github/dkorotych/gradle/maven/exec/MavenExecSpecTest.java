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

import com.github.dkorotych.gradle.maven.DefaultMavenOptions;
import com.github.dkorotych.gradle.maven.MavenOptions;
import com.github.dkorotych.gradle.maven.MemoizedSupplier;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Map.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@SuppressWarnings("OverloadMethodsDeclarationOrder")
class MavenExecSpecTest {
    private static final Supplier<List<PropertyDescriptor>> SPECIFICATION_DESCRIPTORS = MemoizedSupplier.of(() -> {
        final List<String> skip = Arrays.asList("mavenDir", "goals", "options");
        try {
            return Arrays.stream(Introspector.getBeanInfo(MavenExecSpec.class).getPropertyDescriptors())
                    .filter(((Predicate<PropertyDescriptor>) d -> skip.contains(d.getName())).negate())
                    .collect(Collectors.toList());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    });
    private static final Supplier<List<PropertyDescriptor>> OPTION_DESCRIPTORS = MemoizedSupplier.of(() -> {
        try {
            return Arrays.asList(Introspector.getBeanInfo(MavenOptions.class).getPropertyDescriptors());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    });

    public static List<PropertyDescriptor> options() {
        return SPECIFICATION_DESCRIPTORS.get();
    }

    private static void validate(PropertyDescriptor descriptor, MavenExecSpec specification, MavenOptions options,
                                 Object expected) throws Exception {
        Method readMethod = descriptor.getReadMethod();
        assertThat(readMethod.invoke(specification))
                .isEqualTo(expected);
        readMethod = OPTION_DESCRIPTORS.get().stream()
                .filter(propertyDescriptor -> propertyDescriptor.getName().equals(descriptor.getName()))
                .findFirst()
                .map(PropertyDescriptor::getReadMethod)
                .orElseThrow(RuntimeException::new);
        assertThat(readMethod.invoke(options))
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource
    void options(PropertyDescriptor descriptor) throws Exception {
        final MavenExecSpec specification = spy(MavenExecSpec.class);
        final MavenOptions options = spy(DefaultMavenOptions.class);
        doReturn(options).when(specification).getOptions();
        final Object value;

        switch (descriptor.getPropertyType().getSimpleName()) {
            case "boolean" -> {
                validate(descriptor, specification, options, false);
                descriptor.getWriteMethod().invoke(specification, true);
                validate(descriptor, specification, options, true);
            }
            case "String" -> {
                validate(descriptor, specification, options, null);
                value = RandomStringUtils.randomAlphanumeric(10);
                descriptor.getWriteMethod().invoke(specification, value);
                validate(descriptor, specification, options, value);
            }
            case "File" -> {
                validate(descriptor, specification, options, null);
                value = SystemUtils.getJavaIoTmpDir();
                descriptor.getWriteMethod().invoke(specification, value);
                validate(descriptor, specification, options, value);
            }
            case "String[]" -> {
                validate(descriptor, specification, options, null);
                value = new String[]{randomAlphanumeric(10), randomAlphanumeric(10), randomAlphanumeric(10)};
                descriptor.getWriteMethod().invoke(specification, value);
                validate(descriptor, specification, options, value);
            }
            case "Map" -> {
                validate(descriptor, specification, options, null);
                value = of(randomAlphanumeric(8), randomAlphanumeric(4), randomAlphanumeric(5), randomAlphanumeric(8));
                descriptor.getWriteMethod().invoke(specification, value);
                validate(descriptor, specification, options, value);
            }
            default -> fail("Unsupported property type - %s", descriptor.getPropertyType());
        }
    }
}
