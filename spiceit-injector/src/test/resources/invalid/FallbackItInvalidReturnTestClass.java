import gg.neko.spiceit.annotation.FallbackIt;

public class FallbackItInvalidReturnTestClass {

    @FallbackIt(fallbackMethod = "fallbackMethod", fallbackOnNull = true)
    public String testMethod(Integer testParam) {
        throw new RuntimeException("test exception");
    }

    public void fallbackMethod(Integer testParam) {
    }

}
