package com.github.dkorotych.gradle.maven.cli.bindable;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a groovy property or a class.
 *
 * When annotating a property it indicates that the property should be a bound property according to the JavaBeans
 * spec, announcing to listeners that the value has changed and additional wrapper for setter
 *
 * <pre>
 * public class Person {
 *     &#064;groovy.beans.Bindable
 *     private java.lang.String firstName
 *
 *     public void setFirstName(java.lang.String value) {
 *         this.firePropertyChange('firstName', firstName, firstName = value )
 *     }
 *
 *     public void firstName(java.lang.String value) {
 *         this.setFirstName(value)
 *     }
 * }
 * </pre>
 *
 * @see PropertyBindableASTTransformation
 * @see groovy.beans.Bindable
 * @see groovy.beans.BindableASTTransformation
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@java.lang.annotation.Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.TYPE})
@GroovyASTTransformationClass("com.github.dkorotych.gradle.maven.cli.bindable.PropertyBindableASTTransformation")
public @interface PropertyBindable {
}
