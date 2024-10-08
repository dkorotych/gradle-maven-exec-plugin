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

import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

class MavenPropertiesTest {
    @Test
    void asBean() {
        assertThat(MavenProperties.class)
                .satisfies(
                        arg -> assertThat(arg, hasValidBeanConstructor()),
                        arg -> assertThat(arg, hasValidGettersAndSetters()),
                        arg -> assertThat(arg, hasValidBeanHashCode()),
                        arg -> assertThat(arg, hasValidBeanEquals()),
                        arg -> assertThat(arg, hasValidBeanToString())
                );
    }
}
