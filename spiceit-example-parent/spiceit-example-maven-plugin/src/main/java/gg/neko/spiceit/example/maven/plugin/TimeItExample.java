package gg.neko.spiceit.example.maven.plugin;

import gg.neko.spiceit.annotation.TimeIt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TimeItExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeItExample.class);
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static void runExample() {
        LOGGER.info("{}", timeThisMethodWithArgsWithReturn());
        LOGGER.info("{}", doNotTimeThisMethod());
        timeThisVoidMethodWithArgsWithoutReturn();
        doNotTimeThisVoidMethod();
        LOGGER.info("{}", timeThisMethodWithOneParamWithArgs("John"));
        LOGGER.info("{}", timeThisMethodWithOneParamWithoutArgsWithReturn("Harry"));
        LOGGER.info("{}", doNotTimeThisMethodWithOneParam("Karen"));

        try {
            timeThisThrowingMethodWithArgs();
        } catch (Exception e) {
            LOGGER.info("should have timed exception: {}", e.getMessage());
        }

        try {
            doNotTimeThisThrowingMethod();
        } catch (Exception e) {
            LOGGER.info("should NOT have timed exception: {}", e.getMessage());
        }

        try {
            timeThisThrowingMethodWithOneParamWithoutArgs("Mark");
        } catch (Exception e) {
            LOGGER.info("should have timed exception: {}", e.getMessage());
        }

        try {
            doNotTimeThisThrowingMethodWithOneParam("Tom");
        } catch (Exception e) {
            LOGGER.info("should NOT have timed exception: {}", e.getMessage());
        }
    }

    @TimeIt(logArgs = true, logReturn = true)
    private static int timeThisMethodWithArgsWithReturn() {
        sleep();
        return 42;
    }

    private static int doNotTimeThisMethod() {
        return 69;
    }

    @TimeIt(logArgs = true)
    private static void timeThisVoidMethodWithArgsWithoutReturn() {
        sleep();
    }

    private static void doNotTimeThisVoidMethod() { }

    @TimeIt(logArgs = true)
    private static String timeThisMethodWithOneParamWithArgs(String param) {
        sleep();
        return "Hello, " + param;
    }

    @TimeIt(logReturn = true)
    private static String timeThisMethodWithOneParamWithoutArgsWithReturn(String param) {
        sleep();
        return "Well hello, " + param;
    }

    private static String doNotTimeThisMethodWithOneParam(String param) { return "Bye, " + param; }

    @TimeIt(logArgs = true)
    private static void timeThisThrowingMethodWithArgs() {
        sleep();
        throw new RuntimeException("runtime exception!");
    }

    private static void doNotTimeThisThrowingMethod() { throw new RuntimeException("another runtime exception!"); }

    @TimeIt
    private static void timeThisThrowingMethodWithOneParamWithoutArgs(String param) {
        sleep();
        throw new RuntimeException("runtime exception: " + param);
    }

    private static void doNotTimeThisThrowingMethodWithOneParam(String param) { throw new RuntimeException("another runtime exception: " + param); }

    private static void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(RANDOM.nextInt(1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
