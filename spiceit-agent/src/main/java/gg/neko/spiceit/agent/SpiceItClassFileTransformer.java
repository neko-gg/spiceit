package gg.neko.spiceit.agent;

import gg.neko.spiceit.injector.SpiceItInjector;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class SpiceItClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {
        return SpiceItInjector.revise(classfileBuffer);
    }

}
