/*
 * Copyright 2016 Dmitry Korotych
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
package com.github.dkorotych.gradle.maven.cli.bindable

import groovy.test.GroovyAssert
import org.junit.Test

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
class PropertyBindableASTTransformationTest {

    @Test
    void createSetterMethod() {
        GroovyAssert.assertScript('''
package com.github.dkorotych.gradle.maven.cli.bindable

@PropertyBindable
class Person {
    String firstName
    String lastName
}

['setFirstName', 'getFirstName', 'firstName', 'setLastName', 'getLastName', 'lastName'].each { String name ->
    assert hasMethodByName(name)
}

Person person = new Person(firstName: 'Ivan', lastName: 'Ivanov')
assert person.firstName == 'Ivan'
person.firstName('Sidor')
assert person.getFirstName() == 'Sidor'
person.lastName = 'Sidorov'
assert person.getLastName() == 'Sidorov'
person.setFirstName('Ivan')
assert person.getFirstName() == 'Ivan'

static boolean hasMethodByName(String name) {
    Person.metaClass.methods.any { it.name == name }
}
''')
    }


}
