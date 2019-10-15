package gg.neko.spiceit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimeIt {

    int order() default 9000;

    boolean logArgs() default false;

    boolean logReturn() default false;

}
