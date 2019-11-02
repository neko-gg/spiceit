import gg.neko.spiceit.annotation.LogIt;

public class LogItInvalidEntryTestClass {

    @LogIt(entryPattern = "\" + invalid-code + \"")
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
