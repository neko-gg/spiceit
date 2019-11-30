package gg.neko.spiceit.injector.fallbackit;

import gg.neko.spiceit.annotation.FallbackIt;
import javassist.CtMethod;

/**
 * Interface for injectors handling @{@link FallbackIt}.
 */
public interface FallbackItInjector {

    /**
     * Invokes a fallback method when a method completes exceptionally
     * according to {@code fallbackIt} settings.
     *
     * @param fallbackIt instance of {@link FallbackIt} with chosen settings
     * @param ctMethod   the method that, when completing exceptionally, should invoke the fallback method
     */
    void inject(FallbackIt fallbackIt, CtMethod ctMethod);

}
