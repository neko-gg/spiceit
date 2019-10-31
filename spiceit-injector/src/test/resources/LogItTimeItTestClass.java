import gg.neko.spiceit.annotation.LogIt;
import gg.neko.spiceit.annotation.TimeIt;

public class LogItTimeItTestClass {

    @LogIt
    @TimeIt
    public String testMethod(Integer testParam) {
        return String.valueOf(testParam);
    }

}
