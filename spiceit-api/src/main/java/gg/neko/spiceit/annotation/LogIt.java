package gg.neko.spiceit.annotation;

import gg.neko.spiceit.enumeration.LogLevel;
import gg.neko.spiceit.util.Patterns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogIt {

    int order() default 10000;

    String entryPattern() default Patterns.LOGIT_ENTRY_DEFAULT;

    LogLevel entryLogLevel() default LogLevel.INFO;

    String errorPattern() default Patterns.LOGIT_ERROR_DEFAULT;

    LogLevel errorLogLevel() default LogLevel.ERROR;

    String exitPattern() default Patterns.LOGIT_EXIT_DEFAULT;

    LogLevel exitLogLevel() default LogLevel.INFO;

}
