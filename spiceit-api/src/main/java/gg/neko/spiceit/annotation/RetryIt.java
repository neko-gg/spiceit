package gg.neko.spiceit.annotation;

public @interface RetryIt {

    int order() default 2000;

}
