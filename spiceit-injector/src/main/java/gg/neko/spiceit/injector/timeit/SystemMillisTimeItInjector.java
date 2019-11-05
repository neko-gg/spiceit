package gg.neko.spiceit.injector.timeit;

import gg.neko.spiceit.annotation.TimeIt;
import gg.neko.spiceit.injector.InjectorUtils;
import gg.neko.spiceit.injector.PatternSolver;
import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * A {@link TimeItInjector} implementation that uses {@link System#currentTimeMillis()}.
 */
public class SystemMillisTimeItInjector implements TimeItInjector {

    /**
     * {@inheritDoc}
     */
    @Override
    public void inject(TimeIt timeIt, CtMethod ctMethod) {
        CtField ctLoggerField = InjectorUtils.getLoggerField(ctMethod.getDeclaringClass());

        String solvedPattern = PatternSolver.solve(timeIt.logPattern(), ctMethod, "(java.lang.System.currentTimeMillis() - $1)", 1);
        SystemMillisTimeItInjector.delegateCtMethod(ctMethod);
        SystemMillisTimeItInjector.timeCtMethod(timeIt, ctMethod, solvedPattern, ctLoggerField);
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
