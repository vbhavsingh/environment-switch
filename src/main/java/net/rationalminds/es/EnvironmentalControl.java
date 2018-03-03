package net.rationalminds.es;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * Annotation for environment switching. Use this annotation over methods which you want to behave differently 
 * in case of development or testing environment. Annotation value requires fully qualified method name.
 * if alternate method requires no argument then value must be a.b.c.MyClass.myMethod() 
 * if alternate method requires argument values of annotated method then value must be a.b.c.MyClass.myMethod(args) 
 * if alternate method requires reference of 'this' values of annotated method then value must be a.b.c.MyClass.myMethod(this)
 * @author Vaibhav Pratap Singh
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface EnvironmentalControl {
  String devMethod() default "";
  String testMethod() default "";
}
