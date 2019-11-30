import gg.neko.spiceit.annotation.FallbackIt;

public class FallbackItInvalidMethodTestClass {

    @FallbackIt(fallbackMethod = "fallbackMethod")
    public String testMethod(Integer testParam) {
        throw new RuntimeException("test exception");
    }

}
