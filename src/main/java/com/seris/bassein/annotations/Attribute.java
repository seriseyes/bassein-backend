package com.seris.bassein.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {
    String value();

    String descDev() default "";

    boolean skip() default false;

    boolean trim() default false;

    boolean noSpace() default false;

    boolean capitalize() default false;

    boolean upper() default false;

    boolean lower() default false;

    boolean notZero() default false;
}
