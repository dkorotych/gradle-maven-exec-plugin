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
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("OverloadMethodsDeclarationOrder")
class MavenOptionsToCommandLineAdapterTest {
    private static final PropertyDescriptor[] DESCRIPTORS;
    private static final String DEMO = "demo";

    static {
        try {
            DESCRIPTORS = Introspector.getBeanInfo(DefaultMavenOptions.class).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Arguments> validateBoolean() {
        return filterDescriptors(boolean.class)
                .flatMap(descriptor -> {
                    final String option = createOptionName(descriptor);
                    return Stream.of(
                            Arguments.of(descriptor, true, Collections.singletonList(option)),
                            Arguments.of(descriptor, false, Collections.emptyList())
                    );
                });
    }

    public static Stream<Arguments> validateDefine() {
        return Stream.of(
                Arguments.of(null, Collections.emptyList()),
                Arguments.of(new HashMap<>(), Collections.emptyList()),
                Arguments.of(Collections.emptyMap(), Collections.emptyList()),
                Arguments.of(Map.of(DEMO, ""), Collections.emptyList()),
                Arguments.of(Map.of(DEMO, "test"), Collections.singletonList("-Ddemo=test")),
                Arguments.of(Map.of("1", "2", "3", "", "5", "6"), of("-D1=2", "-D5=6")),
                Arguments.of(Map.of("1", "2", "", "4", "5", "6"), of("-D1=2", "-D5=6"))
        );
    }

    public static Stream<Arguments> validateStringArray() {
        return filterDescriptors(String[].class)
                .flatMap(descriptor -> {
                    final String option = createOptionName(descriptor);
                    return Stream.of(
                            Arguments.of(descriptor, null, Collections.emptyList()),
                            Arguments.of(descriptor, new String[0], Collections.emptyList()),
                            Arguments.of(descriptor, new String[]{null, null, null}, Collections.emptyList()),
                            Arguments.of(descriptor, new String[]{DEMO, null, "   ", "test"}, of(option, "demo,test"))
                    );
                });
    }

    public static Stream<Arguments> validateFile() {
        final File tmpDir = SystemUtils.getJavaIoTmpDir();
        final File file = SystemUtils.getJavaHome()
                .toPath()
                .resolve("bin")
                .resolve("java")
                .toFile();
        return filterDescriptors(File.class)
                .flatMap(descriptor -> {
                    final String option = createOptionName(descriptor);
                    String tmpPath = tmpDir.getAbsolutePath();
                    if (StringUtils.containsWhitespace(tmpPath)) {
                        tmpPath = '"' + tmpPath + '"';
                    }
                    String userPath = file.getAbsolutePath();
                    if (StringUtils.containsWhitespace(userPath)) {
                        userPath = '"' + userPath + '"';
                    }
                    return Stream.of(
                            Arguments.of(descriptor, null, Collections.emptyList()),
                            Arguments.of(descriptor, tmpDir, of(option, tmpPath)),
                            Arguments.of(descriptor, file, of(option, userPath))
                    );
                });
    }

    public static Stream<Arguments> validateString() {
        return filterDescriptors(String.class)
                .flatMap(descriptor -> {
                    final String option = createOptionName(descriptor);
                    final String random = TestUtility.randomAlphabetic(20);
                    final String textWithSpaces = "\"text with spaces\"";
                    return Stream.of(
                            Arguments.of(descriptor, null, Collections.emptyList()),
                            Arguments.of(descriptor, "", Collections.emptyList()),
                            Arguments.of(descriptor, "\t \n \n\n    ", Collections.emptyList()),
                            Arguments.of(descriptor, DEMO, of(option, DEMO)),
                            Arguments.of(descriptor, random, of(option, random)),
                            Arguments.of(descriptor, "text with spaces", of(option, textWithSpaces)),
                            Arguments.of(descriptor, textWithSpaces, of(option, textWithSpaces))
                    );
                });
    }

    private static Stream<PropertyDescriptor> filterDescriptors(Class<?> typeClass) {
        return Arrays.stream(DESCRIPTORS)
                .filter(descriptor -> descriptor.getPropertyType().equals(typeClass));
    }

    static String createOptionName(PropertyDescriptor descriptor) {
        return "--" + descriptor.getName()
                .replaceAll("([A-Z])", "-$1")
                .toLowerCase();
    }

    @Test
    void nullOptions() {
        assertThatThrownBy(() -> new MavenOptionsToCommandLineAdapter(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Maven options should be not null");
    }

    @ParameterizedTest
    @MethodSource
    void validateBoolean(PropertyDescriptor property, boolean value, List<String> expected) throws Exception {
        validateOption(property, value, expected, null);
    }

    @ParameterizedTest
    @MethodSource("validateBoolean")
    void validateBooleanWithSupportedOptions(PropertyDescriptor property, boolean value, List<String> expected)
            throws Exception {
        validateOption(property, value, Collections.emptyList(), Collections.emptySet());
    }

    @ParameterizedTest
    @MethodSource
    void validateDefine(Map<String, String> define, List<String> expected) {
        final MavenOptions options = new DefaultMavenOptions();
        options.setDefine(define);
        final MavenOptionsToCommandLineAdapter adapter = new MavenOptionsToCommandLineAdapter(options, null);
        assertThat(adapter.asCommandLine())
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @ParameterizedTest
    @MethodSource
    void validateStringArray(PropertyDescriptor property, String[] value, List<String> expected) throws Exception {
        validateOption(property, value, expected, null);
    }

    @ParameterizedTest
    @MethodSource("validateStringArray")
    void validateStringArrayWithSupportedOptions(PropertyDescriptor property, String[] value, List<String> expected)
            throws Exception {
        validateOption(property, value, Collections.emptyList(), Collections.emptySet());
    }

    @ParameterizedTest
    @MethodSource
    void validateFile(PropertyDescriptor property, File value, List<String> expected) throws Exception {
        validateOption(property, value, expected, null);
    }

    @ParameterizedTest
    @MethodSource("validateFile")
    void validateFileWithSupportedOptions(PropertyDescriptor property, File value, List<String> expected)
            throws Exception {
        validateOption(property, value, Collections.emptyList(), Collections.emptySet());
    }

    @ParameterizedTest
    @MethodSource
    void validateString(PropertyDescriptor property, String value, List<String> expected) throws Exception {
        validateOption(property, value, expected, null);
    }

    @ParameterizedTest
    @MethodSource("validateString")
    void validateStringWithSupportedOptions(PropertyDescriptor property, String value, List<String> expected)
            throws Exception {
        validateOption(property, value, Collections.emptyList(), Collections.emptySet());
    }

    private void validateOption(PropertyDescriptor property, Object value, List<String> expected,
                                Set<String> supportedOptions) throws IllegalAccessException, InvocationTargetException {
        final MavenOptions options = new DefaultMavenOptions();
        property.getWriteMethod().invoke(options, value);
        final MavenOptionsToCommandLineAdapter adapter
                = new MavenOptionsToCommandLineAdapter(options, supportedOptions);
        assertThat(adapter.asCommandLine())
                .isEqualTo(expected);
    }
}
