package gg.neko.spiceit.injector.logit;

import gg.neko.spiceit.annotation.LogIt;
import javassist.CtMethod;

/**
 * Interface for injectors handling @{@link LogIt}.
 */
public interface LogItInjector {

    /**
     * Logs {@code ctMethod} entry, exit, and error according to
     * {@code logIt} settings.
     *
     * @param logIt    instance of {@link LogIt} with chosen settings
     * @param ctMethod the method of which to log entry, exit, and error
     */
    void inject(LogIt logIt, CtMethod ctMethod);

}
