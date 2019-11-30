package gg.neko.spiceit.example.maven.plugin;

import gg.neko.spiceit.annotation.FallbackIt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FallbackItExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(FallbackItExample.class);

    public static void runExample() {
        LOGGER.info("invoking a throwing method annotated with @FallbackIt: {}", fallbackThisMethod());

        try {
            doNotFallbackThisMethod();
        } catch (Exception e) {
            LOGGER.info("invoking a throwing method NOT annotated with @FallbackIt: {}", e.getMessage());
        }

        LOGGER.info("invoking a method returning null annotated with @FallbackIt and fallbackOnNull set to true: {}", fallbackThisMethodOnNull());
        LOGGER.info("invoking a method returning null annotated with @FallbackIt and fallbackOnNull set to false: {}", doNotFallbackThisMethodOnNull());

        try {
            doNotFallbackThisMethodWithIgnoredException();
        } catch (Exception e) {
            LOGGER.info("invoking a throwing method annotated with @FallbackIt whose thrown exception is set in ignoredExceptions: {}", e.getMessage());
        }

        try {
            doNotFallbackForThisMethodWithWrongTriggeringException();
        } catch (Exception e) {
            LOGGER.info("invoking a throwing method annotated with @FallbackIt whose thrown exception is NOT set in triggeringExceptions: {}", e.getMessage());
        }

        try {
            LOGGER.info("invoking a throwing method annotated with @FallbackIt whose thrown exception is set in triggeringExceptions: {}", fallbackForThisMethodWithCorrectTriggeringException());
        } catch (IOException ignored) { }

        LOGGER.info("invoking a throwing method annotated with @FallbackIt with extra triggeringExceptions: {}", fallbackForThisMethodWithExtraTriggeringException());
    }

    @FallbackIt(fallbackMethod = "fallbackMethod")
    private static String fallbackThisMethod() {
        throw new RuntimeException("runtime exception!");
    }

    private static String doNotFallbackThisMethod() {
        throw new RuntimeException("runtime exception!");
    }

    @FallbackIt(fallbackMethod = "fallbackMethod", fallbackOnNull = true)
    private static String fallbackThisMethodOnNull() {
        return null;
    }

    @FallbackIt(fallbackMethod = "fallbackMethod")
    private static String doNotFallbackThisMethodOnNull() {
        return null;
    }

    @FallbackIt(fallbackMethod = "fallbackMethod", ignoredExceptions = RuntimeException.class)
    private static String doNotFallbackThisMethodWithIgnoredException() {
        throw new RuntimeException("runtime exception!");
    }

    @FallbackIt(fallbackMethod = "fallbackMethod", triggeringExceptions = IOException.class)
    private static String doNotFallbackForThisMethodWithWrongTriggeringException() {
        throw new RuntimeException("runtime exception!");
    }

    @FallbackIt(fallbackMethod = "fallbackMethod", triggeringExceptions = IOException.class)
    private static String fallbackForThisMethodWithCorrectTriggeringException() throws IOException {
        throw new IOException("io exception!");
    }

    @FallbackIt(fallbackMethod = "fallbackMethod", triggeringExceptions = {IOException.class, RuntimeException.class})
    private static String fallbackForThisMethodWithExtraTriggeringException() {
        throw new RuntimeException("runtime exception!");
    }

    private static String fallbackMethod() {
        return "nyan";
    }

}
