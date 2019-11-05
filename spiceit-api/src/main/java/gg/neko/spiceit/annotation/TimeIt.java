package gg.neko.spiceit.annotation;

import gg.neko.spiceit.enumeration.LogLevel;
import gg.neko.spiceit.util.Patterns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Logs the execution time of a method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimeIt {

    /**
     * If a method has multiple SpiceIt annotations, they are processed
     * sequentially in this order (ascending).
     *
     * @return the processing order of this annotation
     */
    int order() default 9000;

    /**
     * The pattern to use when logging this method execution time.
     * See {@link Patterns} for examples.
     *
     * @return the log pattern for this method execution time
     * @see Patterns
     */
    String logPattern() default Patterns.TIMEIT_DEFAULT;

    /**
     * The {@link LogLevel} at which to log this method execution time.
     *
     * @return the log level for this method execution time
     * @see LogLevel
     */
    LogLevel logLevel() default LogLevel.INFO;

}
