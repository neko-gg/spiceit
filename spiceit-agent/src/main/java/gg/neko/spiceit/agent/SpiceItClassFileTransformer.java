package gg.neko.spiceit.agent;

import gg.neko.spiceit.injector.SpiceItInjector;
import gg.neko.spiceit.injector.logit.LogItInjector;
import gg.neko.spiceit.injector.logit.LogItInjectorType;
import gg.neko.spiceit.injector.timeit.TimeItInjector;
import gg.neko.spiceit.injector.timeit.TimeItInjectorType;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class SpiceItClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {
        if (className.startsWith("java/")) { return classfileBuffer; }

        return SpiceItInjector.builder()
                              .logItInjector(getLogItInjector())
                              .timeItInjector(getTimeItInjector())
                              .build()
                              .revise(classfileBuffer);
    }

    private LogItInjector getLogItInjector() {
        return LogItInjectorType.SLF4J.getLogItInjector();
    }

    private TimeItInjector getTimeItInjector() {
        return TimeItInjectorType.SYSTEM_MILLIS.getTimeItInjector();
    }

}
