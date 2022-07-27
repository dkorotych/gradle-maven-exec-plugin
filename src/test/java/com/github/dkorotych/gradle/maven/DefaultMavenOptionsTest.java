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
package com.github.dkorotych.gradle.maven;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.google.code.beanmatchers.BeanMatchers.*;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class DefaultMavenOptionsTest {
    public static Stream<PropertyDescriptor> lookLikeAPropertyAccess() throws IntrospectionException {
        return Arrays.stream(Introspector.getBeanInfo(DefaultMavenOptions.class, Object.class).getPropertyDescriptors());
    }

    @Test
    void asBean() {
        assertThat(DefaultMavenOptions.class, allOf(
                hasValidBeanConstructor(),
                hasValidGettersAndSetters(),
                hasValidBeanHashCode(),
                hasValidBeanEquals(),
                hasValidBeanToString()
        ));
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
            case "boolean":
                for (boolean value : asList(true, false)) {
                    resetToDefault(options, descriptor);
                    validateSetter(descriptor, options, value);
                    resetToDefault(options, descriptor);
                    validatePropertySetter(descriptor, options, value);
                }
                break;
            case "String":
                resetToDefault(options, descriptor);
                validateSetter(descriptor, options, random(10));
                resetToDefault(options, descriptor);
                validatePropertySetter(descriptor, options, random(10));
                break;
            case "File":
                resetToDefault(options, descriptor);
                validateSetter(descriptor, options, SystemUtils.getJavaIoTmpDir());
                resetToDefault(options, descriptor);
                validatePropertySetter(descriptor, options, SystemUtils.getUserHome());
                break;
            case "String[]":
                resetToDefault(options, descriptor);
                validateSetter(descriptor, options, new String[]{random(10), random(10), random(10)});
                resetToDefault(options, descriptor);
                validatePropertySetter(descriptor, options, new String[]{random(10), random(10), random(10)});
                break;
            case "Map":
                resetToDefault(options, descriptor);
                validateSetter(descriptor, options, ImmutableMap.of(random(10), random(10), random(5), random(8)));
                resetToDefault(options, descriptor);
                validatePropertySetter(descriptor, options, ImmutableMap.of(random(10), random(10), random(5), random(8)));
                break;
            default:
                fail("Unsupported property type - %s", descriptor.getPropertyType());
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

    private void validateSetter(PropertyDescriptor descriptor, DefaultMavenOptions options, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        descriptor.getWriteMethod().invoke(options, value);
        assertThat(descriptor.getReadMethod().invoke(options))
                .isEqualTo(value);
        assertThat(getPropertyReader(descriptor).invoke(options))
                .isEqualTo(value);
    }

    private void validatePropertySetter(PropertyDescriptor descriptor, DefaultMavenOptions options, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        getPropertyWriter(descriptor).invoke(options, value);
        assertThat(getPropertyReader(descriptor).invoke(options))
                .isEqualTo(value);
        assertThat(descriptor.getReadMethod().invoke(options))
                .isEqualTo(value);
    }
}