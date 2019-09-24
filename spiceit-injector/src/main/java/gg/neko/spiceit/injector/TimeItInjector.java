package gg.neko.spiceit.injector;

import gg.neko.spiceit.annotation.TimeIt;
import javassist.CtMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeItInjector {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeItInjector.class);

    private TimeItInjector() {}

    public static void inject(TimeIt timeIt, CtMethod ctMethod) {
        LOGGER.info("[TimeIt] {}", ctMethod.getName());
    }

}
