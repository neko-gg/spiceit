package gg.neko.spiceit.injector.logit;

import gg.neko.spiceit.annotation.LogIt;
import javassist.CtMethod;

public interface LogItInjector {

    void inject(LogIt logIt, CtMethod ctMethod);

}
