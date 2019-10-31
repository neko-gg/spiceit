import gg.neko.spiceit.annotation.LogIt;

public class LogItTestClass {

    @LogIt
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
