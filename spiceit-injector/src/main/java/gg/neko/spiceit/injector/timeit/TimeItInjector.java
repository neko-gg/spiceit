package gg.neko.spiceit.injector.timeit;

import gg.neko.spiceit.annotation.TimeIt;
import javassist.CtMethod;

public interface TimeItInjector {

    void inject(TimeIt timeIt, CtMethod ctMethod);

}
