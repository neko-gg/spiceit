import gg.neko.spiceit.annotation.LogIt;

public class LogItInvalidExitTestClass {

    @LogIt(exitPattern = "\" + invalid-code + \"")
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
