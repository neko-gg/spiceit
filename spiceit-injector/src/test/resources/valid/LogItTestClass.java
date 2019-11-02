import gg.neko.spiceit.annotation.LogIt;

public class LogItTestClass {

    @LogIt
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

    @LogIt
    public String testExceptionMethod(Integer testParam) {
        throw new RuntimeException("test exception");
    }

    public String testNotAnnotatedMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
