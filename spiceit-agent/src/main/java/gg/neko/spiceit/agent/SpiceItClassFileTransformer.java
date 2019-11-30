package gg.neko.spiceit.agent;

import gg.neko.spiceit.injector.SpiceItInjector;
import gg.neko.spiceit.injector.fallbackit.FallbackItInjector;
import gg.neko.spiceit.injector.fallbackit.FallbackItInjectorType;
import gg.neko.spiceit.injector.logit.LogItInjector;
import gg.neko.spiceit.injector.logit.LogItInjectorType;
import gg.neko.spiceit.injector.timeit.TimeItInjector;
import gg.neko.spiceit.injector.timeit.TimeItInjectorType;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Arrays;

public class SpiceItClassFileTransformer implements ClassFileTransformer {

    private final SpiceItInjector spiceItInjector;

    public SpiceItClassFileTransformer() {
        this.spiceItInjector = SpiceItInjector.builder()
                                              .logItInjector(getLogItInjector())
                                              .timeItInjector(getTimeItInjector())
                                              .fallbackItInjector(getFallbackItInjector())
                                              .build();
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {
        if (className.startsWith("java/")) { return null; }

        try {
            return this.spiceItInjector.revise(Arrays.copyOf(classfileBuffer, classfileBuffer.length));
        } catch (Throwable e) {
            System.err.println("[SpiceIt] failed to revise class: " + className);
            e.printStackTrace();
            throw e;
        }
    }

    private LogItInjector getLogItInjector() {
        return LogItInjectorType.SLF4J.getLogItInjector();
    }

    private TimeItInjector getTimeItInjector() {
        return TimeItInjectorType.SYSTEM_MILLIS.getTimeItInjector();
    }

    private FallbackItInjector getFallbackItInjector() {
        return FallbackItInjectorType.TRY_CATCH.getFallbackItInjector();
    }

}
