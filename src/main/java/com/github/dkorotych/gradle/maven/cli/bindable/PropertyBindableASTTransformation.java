/**
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
package com.github.dkorotych.gradle.maven.cli.bindable;

import groovy.beans.BindableASTTransformation;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

/**
 * Handles generation of code for the {@code @{@link PropertyBindable}} annotation.
 *
 * @see PropertyBindable
 * @see BindableASTTransformation
 * @see groovy.beans.Bindable
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class PropertyBindableASTTransformation extends BindableASTTransformation {

    @Override
    protected void createSetterMethod(final ClassNode declaringClass, final PropertyNode propertyNode,
            final String setterName, final Statement setterBlock) {
        super.createSetterMethod(declaringClass, propertyNode, setterName, setterBlock);
        final String argumentName = "value";
        final Statement code = stmt(callThisX(setterName, args(varX(argumentName))));
        final MethodNode setter = new MethodNode(
                propertyNode.getName(),
                propertyNode.getModifiers(),
                ClassHelper.VOID_TYPE,
                params(param(propertyNode.getType(), argumentName)),
                ClassNode.EMPTY_ARRAY,
                code);
        setter.setSynthetic(true);
        declaringClass.addMethod(setter);
    }
}
