package gg.neko.spiceit.annotation;

public @interface LogIt {

    int order() default 10000;

    boolean logArgs() default true;

}
