package gg.neko.spiceit.injector;

import gg.neko.spiceit.annotation.LogIt;
import gg.neko.spiceit.injector.exception.SpiceItReviserException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.util.Locale;

public class LogItInjector {

    private LogItInjector() {}

    public static void inject(LogIt logIt, CtMethod ctMethod) {
        boolean logArgs = logIt.logArgs();
        CtField ctLoggerField = InjectorUtils.getLoggerField(ctMethod.getDeclaringClass());

        // todo: these three methods are very similar, try to refactor them
        logEntry(ctMethod, ctLoggerField, logArgs);
        logError(ctMethod, ctLoggerField, logArgs);
        logExit(ctMethod, ctLoggerField, logArgs);
    }

    // todo: make logArgs act as in the other two methods (check twice + object array as logger arg)
    // todo: checking logArgs twice and string-formatting is ugly, try using two separate template strings
    private static void logEntry(CtMethod ctMethod, CtField ctLoggerField, boolean logArgs) {
        try {
            ctMethod.insertBefore(String.format(Locale.US,
                                                "%s.info(\"[ENTRY] class: %s, method: %s%s);",
                                                ctLoggerField.getName(),
                                                ctMethod.getDeclaringClass().getName(),
                                                InjectorUtils.getMethodSignature(ctMethod),
                                                logArgs ? ", args: {}\", java.util.Arrays.toString($args)"
                                                        : "\""));
        } catch (CannotCompileException e) {
            throw new SpiceItReviserException(e);
        }
    }

    private static void logError(CtMethod ctMethod, CtField ctLoggerField, boolean logArgs) {
        try {
            ctMethod.addCatch(String.format(Locale.US,
                                            "%s.info(\"[ERROR] class: %s, method: %s, %sexception: {}\", new java.lang.Object[]{%s$e.getMessage(), $e}); throw $e;",
                                            ctLoggerField.getName(),
                                            ctMethod.getDeclaringClass().getName(),
                                            InjectorUtils.getMethodSignature(ctMethod),
                                            logArgs ? "args: {}, "
                                                    : "",
                                            logArgs ? "java.util.Arrays.toString($args), "
                                                    : ""),
                              getCatchExceptionTypeName());
        } catch (CannotCompileException e) {
            throw new SpiceItReviserException(e);
        }
    }

    private static void logExit(CtMethod ctMethod, CtField ctLoggerField, boolean logArgs) {
        try {
            ctMethod.insertAfter(String.format(Locale.US,
                                               "%s.info(\"[EXIT] class: %s, method: %s, %sreturn: {}\", new java.lang.Object[]{%s($w)$_});",
                                               ctLoggerField.getName(),
                                               ctMethod.getDeclaringClass().getName(),
                                               InjectorUtils.getMethodSignature(ctMethod),
                                               logArgs ? "args: {}, "
                                                       : "",
                                               logArgs ? "java.util.Arrays.toString($args), "
                                                       : ""));
        } catch (CannotCompileException e) {
            throw new SpiceItReviserException(e);
        }
    }

    private static CtClass getCatchExceptionTypeName() {
        try {
            return ClassPool.getDefault().get(Throwable.class.getName());
        } catch (NotFoundException e) {
            throw new SpiceItReviserException(e);
        }
    }

}
