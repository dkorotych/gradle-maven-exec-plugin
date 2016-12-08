package com.github.dkorotych.gradle.maven.cli.bindable;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@java.lang.annotation.Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.TYPE})
@GroovyASTTransformationClass("com.github.dkorotych.gradle.maven.cli.bindable.PropertyBindableASTTransformation")
public @interface PropertyBindable {
}