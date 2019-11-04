import gg.neko.spiceit.annotation.LogIt;

public class LogItInvalidEntryTestClass {

    @LogIt(entryPattern = "${method.args[69]}")
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
