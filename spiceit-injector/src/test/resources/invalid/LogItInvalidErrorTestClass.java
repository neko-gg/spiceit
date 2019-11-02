import gg.neko.spiceit.annotation.LogIt;

public class LogItInvalidErrorTestClass {

    @LogIt(errorPattern = "\" + invalid-code + \"")
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
