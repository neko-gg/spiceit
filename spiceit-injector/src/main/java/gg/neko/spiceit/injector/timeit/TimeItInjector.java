package gg.neko.spiceit.injector.timeit;

import gg.neko.spiceit.annotation.TimeIt;
import javassist.CtMethod;

/**
 * Interface for injectors handling @{@link TimeIt}.
 */
public interface TimeItInjector {

    /**
     * Logs {@code ctMethod} execution time according to
     * {@code timeIt} settings.
     *
     * @param timeIt   instance of {@link TimeIt} with chosen settings
     * @param ctMethod the method of which to log execution time
     */
    void inject(TimeIt timeIt, CtMethod ctMethod);

}
