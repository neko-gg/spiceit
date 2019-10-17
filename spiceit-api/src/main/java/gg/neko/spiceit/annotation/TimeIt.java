package gg.neko.spiceit.annotation;

import gg.neko.spiceit.enumeration.LogLevel;
import gg.neko.spiceit.util.Patterns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimeIt {

    int order() default 9000;

    String logPattern() default Patterns.TIMEIT_DEFAULT;

    LogLevel logLevel() default LogLevel.INFO;

}
