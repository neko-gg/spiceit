import gg.neko.spiceit.annotation.TimeIt;

public class TimeItTestClass {

    @TimeIt
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

    @TimeIt
    public String testExceptionMethod(Integer testParam) {
        throw new RuntimeException("test exception");
    }

    public String testNotAnnotatedMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
