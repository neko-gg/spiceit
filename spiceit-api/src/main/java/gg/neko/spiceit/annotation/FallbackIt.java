package gg.neko.spiceit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Invokes a fallback method when a method completes exceptionally.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FallbackIt {

    /**
     * If a method has multiple SpiceIt annotations, they are processed
     * sequentially in this order (ascending).
     *
     * @return the processing order of this annotation
     */
    int order() default 8000;

    /**
     * The fallback method to invoke.
     * Must have the same parameters (in type, number, and order) as the calling method.
     * Must have the same return type.
     * Must be callable from the calling method (for example, it must be {@code static} if calling method is {@code static}).
     *
     * @return the name of the fallback method to invoke
     */
    String fallbackMethod();

    /**
     * The exception types for which to invoke the fallback method, should the calling method throw them.
     * <br>
     * The fallback method is invoked when the calling method throws an exception contained in {@link #triggeringExceptions}
     * AND not contained in {@link #ignoredExceptions}
     *
     * @return the exception types for which to invoke the fallback method
     */
    Class<? extends Throwable>[] triggeringExceptions() default {Throwable.class};

    /**
     * The exception types for which NOT to invoke the fallback method, should the calling method throw them.
     * <br>
     * The fallback method is invoked when the calling method throws an exception contained in {@link #triggeringExceptions}
     * AND not contained in {@link #ignoredExceptions}
     *
     * @return the exception types for which NOT to invoke the fallback method
     */
    Class<? extends Throwable>[] ignoredExceptions() default {};

    /**
     * Invoke the fallback method when {@code null} is returned.
     *
     * @return whether to invoke the fallback method when {@code null} is returned
     */
    boolean fallbackOnNull() default false;

}
