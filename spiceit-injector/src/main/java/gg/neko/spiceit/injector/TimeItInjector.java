package gg.neko.spiceit.injector;

import gg.neko.spiceit.annotation.TimeIt;
import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class TimeItInjector {

    private TimeItInjector() { throw new UnsupportedOperationException("do not instantiate this class"); }

    public static void inject(TimeIt timeIt, CtMethod ctMethod) {
        CtField ctLoggerField = InjectorUtils.getLoggerField(ctMethod.getDeclaringClass());

        CtMethod delegatorCtMethod = delegateCtMethod(ctMethod);
        timeCtMethod(timeIt, ctMethod, delegatorCtMethod, ctLoggerField);
    }

    private static CtMethod delegateCtMethod(CtMethod ctMethod) {
        try {
            CtClass[] parameterTypes = ctMethod.getParameterTypes();
            String ctMethodName = ctMethod.getName();
            String newCtMethodName = "$SPICEIT_TIMEIT_DELEGATE_" + ctMethodName;
            ctMethod.setName(newCtMethodName);
            ctMethod.insertParameter(CtClass.longType);

            CtMethod delegatorCtMethod = CtNewMethod.make(ctMethod.getModifiers(),
                                                          ctMethod.getReturnType(),
                                                          ctMethodName,
                                                          parameterTypes,
                                                          ctMethod.getExceptionTypes(),
                                                          "return " + newCtMethodName + "(java.lang.System.currentTimeMillis(), $$);",
                                                          ctMethod.getDeclaringClass());

            ctMethod.getDeclaringClass().addMethod(delegatorCtMethod);

            return delegatorCtMethod;
        } catch (NotFoundException | CannotCompileException e) {
            throw new SpiceItInjectorException(e);
        }
    }

    private static void timeCtMethod(TimeIt timeIt, CtMethod delegatedCtMethod, CtMethod delegatorCtMethod, CtField ctLoggerField) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(ctLoggerField.getName())
                     .append(".info(")
                     .append("\"")
                     .append("[TIME] ")
                     .append("[")
                     .append("\" + ")
                     .append("(java.lang.System.currentTimeMillis() - $1)")
                     .append(" + \"")
                     .append("ms] ")
                     .append("class: ")
                     .append(delegatedCtMethod.getDeclaringClass().getName())
                     .append(", ")
                     .append("method: ")
                     .append(InjectorUtils.getMethodSignature(delegatorCtMethod));

        if (timeIt.logArgs()) {
            stringBuilder.append(", ")
                         .append("args: ")
                         .append("{}");
        }

        if (timeIt.logReturn()) {
            stringBuilder.append(", ")
                         .append("return: ")
                         .append("{}");
        }

        stringBuilder.append("\"");

        List<String> loggerArgs = new ArrayList<>();

        if (timeIt.logArgs()) {
            try {
                loggerArgs.add("java.util.Arrays.toString(java.util.Arrays.copyOfRange($args, 1, " + delegatedCtMethod.getParameterTypes().length + "))");
            } catch (NotFoundException e) {
                throw new SpiceItInjectorException(e);
            }
        }

        if (timeIt.logReturn()) {
            loggerArgs.add("($w)$_");
        }

        if (!loggerArgs.isEmpty()) {
            stringBuilder.append(", ")
                         .append("new java.lang.Object[]{")
                         .append(String.join(", ", loggerArgs))
                         .append("}");
        }

        stringBuilder.append(");");

        try {
            delegatedCtMethod.insertAfter(stringBuilder.toString(), true);
        } catch (CannotCompileException e) {
            throw new SpiceItInjectorException(e);
        }
    }

}
