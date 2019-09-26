package gg.neko.spiceit.example.maven.plugin;

import gg.neko.spiceit.annotation.LogIt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogItExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogItExample.class);

    public static void runExample() {
        LOGGER.info("{}", logThisMethod());
        LOGGER.info("{}", doNotLogThisMethod());
        logThisVoidMethod();
        doNotLogThisVoidMethod();
        LOGGER.info("{}", logThisMethodWithOneParam("John"));
        LOGGER.info("{}", logThisMethodWithOneParamWithoutArgs("Harry"));
        LOGGER.info("{}", doNotLogThisMethodWithOneParam("Karen"));

        try {
            logThisThrowingMethod();
        } catch (Exception e) {
            LOGGER.info("should have logged exception: {}", e.getMessage());
        }

        try {
            doNotLogThisThrowingMethod();
        } catch (Exception e) {
            LOGGER.info("should NOT have logged exception: {}", e.getMessage());
        }

        try {
            logThisThrowingMethodWithOneParamWithoutArgs("Mark");
        } catch (Exception e) {
            LOGGER.info("should have logged exception: {}", e.getMessage());
        }

        try {
            doNotLogThisThrowingMethodWithOneParam("Tom");
        } catch (Exception e) {
            LOGGER.info("should NOT have logged exception: {}", e.getMessage());
        }
    }

    @LogIt
    private static int logThisMethod() {
        return 42;
    }

    private static int doNotLogThisMethod() {
        return 69;
    }

    @LogIt
    private static void logThisVoidMethod() { }

    private static void doNotLogThisVoidMethod() { }

    @LogIt
    private static String logThisMethodWithOneParam(String param) { return "Hello, " + param; }

    @LogIt(logArgs = false)
    private static String logThisMethodWithOneParamWithoutArgs(String param) { return "Well hello, " + param; }

    private static String doNotLogThisMethodWithOneParam(String param) { return "Bye, " + param; }

    @LogIt
    private static void logThisThrowingMethod() { throw new RuntimeException("runtime exception!"); }

    private static void doNotLogThisThrowingMethod() { throw new RuntimeException("another runtime exception!"); }

    @LogIt(logArgs = false)
    private static void logThisThrowingMethodWithOneParamWithoutArgs(String param) { throw new RuntimeException("runtime exception: " + param); }

    private static void doNotLogThisThrowingMethodWithOneParam(String param) { throw new RuntimeException("another runtime exception: " + param); }

}
