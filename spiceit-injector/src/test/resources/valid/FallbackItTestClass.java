import gg.neko.spiceit.annotation.FallbackIt;

import java.io.IOException;

public class FallbackItTestClass {

    @FallbackIt(fallbackMethod = "fallbackMethod")
    public String testMethod(Integer testParam) {
        throw new RuntimeException("test exception");
    }

    public String testNotAnnotatedMethod(Integer testParam) {
        throw new RuntimeException("test exception");
    }

    @FallbackIt(fallbackMethod = "fallbackMethod", fallbackOnNull = true, ignoredExceptions = IOException.class)
    public String altTestMethod(Integer testParam) {
        return null;
    }

    public String fallbackMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
