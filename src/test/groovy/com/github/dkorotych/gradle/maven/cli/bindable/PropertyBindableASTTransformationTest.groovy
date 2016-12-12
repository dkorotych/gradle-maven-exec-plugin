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
