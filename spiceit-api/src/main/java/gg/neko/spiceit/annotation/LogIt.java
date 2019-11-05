package gg.neko.spiceit.annotation;

import gg.neko.spiceit.enumeration.LogLevel;
import gg.neko.spiceit.util.Patterns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Logs entry, error and exit of a method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogIt {

    /**
     * If a method has multiple SpiceIt annotations, they are processed
     * sequentially in this order (ascending).
     *
     * @return the processing order of this annotation
     */
    int order() default 10000;

    /**
     * The pattern to use when logging this method entry.
     * See {@link Patterns} for examples.
     *
     * @return the log pattern for this method entry
     * @see Patterns
     */
    String entryPattern() default Patterns.LOGIT_ENTRY_DEFAULT;

    /**
     * The {@link LogLevel} at which to log this method entry.
     *
     * @return the log level for this method entry
     * @see LogLevel
     */
    LogLevel entryLogLevel() default LogLevel.INFO;

    /**
     * The pattern to use when logging this method error.
     * See {@link Patterns} for examples.
     *
     * @return the log pattern for this method error
     * @see Patterns
     */
    String errorPattern() default Patterns.LOGIT_ERROR_DEFAULT;

    /**
     * The {@link LogLevel} at which to log this method error.
     *
     * @return the log level for this method error
     * @see LogLevel
     */
    LogLevel errorLogLevel() default LogLevel.ERROR;

    /**
     * The pattern to use when logging this method exit.
     * See {@link Patterns} for examples.
     *
     * @return the log pattern for this method exit
     * @see Patterns
     */
    String exitPattern() default Patterns.LOGIT_EXIT_DEFAULT;

    /**
     * The {@link LogLevel} at which to log this method exit.
     *
     * @return the log level for this method exit
     * @see LogLevel
     */
    LogLevel exitLogLevel() default LogLevel.INFO;

}
