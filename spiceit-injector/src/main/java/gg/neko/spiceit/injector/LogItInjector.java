package gg.neko.spiceit.injector;

import gg.neko.spiceit.annotation.LogIt;
import gg.neko.spiceit.injector.exception.SpiceItReviserException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class LogItInjector {

    private LogItInjector() {}

    public static void inject(LogIt logIt, CtMethod ctMethod) {
        boolean logArgs = logIt.logArgs();
        CtField ctLoggerField = InjectorUtils.getLoggerField(ctMethod.getDeclaringClass());

        logEntry(ctMethod, ctLoggerField, logArgs);
        logError(ctMethod, ctLoggerField, logArgs);
        logExit(ctMethod, ctLoggerField, logArgs);
    }

    private static void logEntry(CtMethod ctMethod, CtField ctLoggerField, boolean logArgs) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(ctLoggerField.getName())
                     .append(".info(")
                     .append("\"")
                     .append("[ENTRY] ")
                     .append("class: ")
                     .append(ctMethod.getDeclaringClass().getName())
                     .append(", ")
                     .append("method: ")
                     .append(InjectorUtils.getMethodSignature(ctMethod));

        if (logArgs) {
            stringBuilder.append(", ")
                         .append("args: ")
                         .append("{}")
                         .append("\"")
                         .append(", ")
                         .append("java.util.Arrays.toString($args)");
        } else {
            stringBuilder.append("\"");
        }

        stringBuilder.append(");");

        try {
            ctMethod.insertBefore(stringBuilder.toString());
        } catch (CannotCompileException e) {
            throw new SpiceItReviserException(e);
        }
    }

    private static void logError(CtMethod ctMethod, CtField ctLoggerField, boolean logArgs) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(ctLoggerField.getName())
                     .append(".error(")
                     .append("\"")
                     .append("[ERROR] ")
                     .append("class: ")
                     .append(ctMethod.getDeclaringClass().getName())
                     .append(", ")
                     .append("method: ")
                     .append(InjectorUtils.getMethodSignature(ctMethod));

        if (logArgs) {
            stringBuilder.append(", ")
                         .append("args: ")
                         .append("{}");
        }

        stringBuilder.append(", ")
                     .append("exception: ")
                     .append("{}")
                     .append("\"")
                     .append(", ")
                     .append("new java.lang.Object[]{");

        if (logArgs) {
            stringBuilder.append("java.util.Arrays.toString($args)")
                         .append(", ");
        }

        stringBuilder.append("$e.getMessage()")
                     .append(", ")
                     .append("$e")
                     .append("}); ")
                     .append("throw $e;");

        try {
            ctMethod.addCatch(stringBuilder.toString(), getCatchExceptionTypeName());
        } catch (CannotCompileException e) {
            throw new SpiceItReviserException(e);
        }
    }

    private static void logExit(CtMethod ctMethod, CtField ctLoggerField, boolean logArgs) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(ctLoggerField.getName())
                     .append(".info(")
                     .append("\"")
                     .append("[EXIT] ")
                     .append("class: ")
                     .append(ctMethod.getDeclaringClass().getName())
                     .append(", ")
                     .append("method: ")
                     .append(InjectorUtils.getMethodSignature(ctMethod));

        if (logArgs) {
            stringBuilder.append(", ")
                         .append("args: ")
                         .append("{}");
        }

        stringBuilder.append(", ")
                     .append("return: ")
                     .append("{}")
                     .append("\"")
                     .append(", ")
                     .append("new java.lang.Object[]{");

        if (logArgs) {
            stringBuilder.append("java.util.Arrays.toString($args)")
                         .append(", ");
        }

        stringBuilder.append("($w)$_")
                     .append("});");

        try {
            ctMethod.insertAfter(stringBuilder.toString());
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
