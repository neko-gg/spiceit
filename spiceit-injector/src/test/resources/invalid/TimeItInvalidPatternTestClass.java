import gg.neko.spiceit.annotation.TimeIt;

public class TimeItInvalidPatternTestClass {

    @TimeIt(logPattern = "${method.args[666]}")
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
