import gg.neko.spiceit.annotation.TimeIt;

public class TimeItTestClass {

    @TimeIt
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
