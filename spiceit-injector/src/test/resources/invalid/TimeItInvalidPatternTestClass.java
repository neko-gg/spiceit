import gg.neko.spiceit.annotation.TimeIt;

public class TimeItInvalidPatternTestClass {

    @TimeIt(logPattern = "\" + invalid-code + \"")
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
