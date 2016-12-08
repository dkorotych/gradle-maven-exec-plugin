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

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class PropertyBindableASTTransformation extends BindableASTTransformation {
    @Override
    protected void createSetterMethod(ClassNode declaringClass, PropertyNode propertyNode, String setterName,
                                      Statement setterBlock) {
        super.createSetterMethod(declaringClass, propertyNode, setterName, setterBlock);
        Statement code = stmt(callThisX(setterName, args(varX("value"))));
        MethodNode setter = new MethodNode(
                propertyNode.getName(),
                propertyNode.getModifiers(),
                ClassHelper.VOID_TYPE,
                params(param(propertyNode.getType(), "value")),
                ClassNode.EMPTY_ARRAY,
                code);
        setter.setSynthetic(true);
        declaringClass.addMethod(setter);
    }
}
