package gg.neko.spiceit.injector;

import gg.neko.spiceit.annotation.TimeIt;
import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class TimeItInjector {

    private TimeItInjector() { throw new UnsupportedOperationException("do not instantiate this class"); }

    public static void inject(TimeIt timeIt, CtMethod ctMethod) {
        CtField ctLoggerField = InjectorUtils.getLoggerField(ctMethod.getDeclaringClass());

        String solvedPattern = PatternSolver.solve(timeIt.logPattern(), ctMethod, "$1", 1);
        delegateCtMethod(ctMethod);
        timeCtMethod(timeIt, ctMethod, solvedPattern, ctLoggerField);
    }

    private static void delegateCtMethod(CtMethod ctMethod) {
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
        } catch (NotFoundException | CannotCompileException e) {
            throw new SpiceItInjectorException(e);
        }
    }

    private static void timeCtMethod(TimeIt timeIt, CtMethod ctMethod, String solvedPattern, CtField ctLoggerField) {
        String logStatement = InjectorUtils.logPattern(timeIt.logLevel(),
                                                       ctLoggerField.getName(),
                                                       solvedPattern);

        try {
            ctMethod.insertAfter(logStatement, true);
        } catch (CannotCompileException e) {
            throw new SpiceItInjectorException(e);
        }
    }

}
