import gg.neko.spiceit.annotation.LogIt;

public class LogItInvalidExitTestClass {

    @LogIt(exitPattern = "${method.args[69]}")
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
