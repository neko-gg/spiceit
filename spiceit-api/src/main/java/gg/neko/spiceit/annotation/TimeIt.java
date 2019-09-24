package gg.neko.spiceit.annotation;

public @interface TimeIt {

    int order() default 9000;

    boolean logArgs() default false;

}
