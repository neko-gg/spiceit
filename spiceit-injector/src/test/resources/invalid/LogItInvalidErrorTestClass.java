import gg.neko.spiceit.annotation.LogIt;

public class LogItInvalidErrorTestClass {

    @LogIt(errorPattern = "${method.args[69]}")
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
