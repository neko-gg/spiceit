package gg.neko.spiceit.injector.logit;

import gg.neko.spiceit.annotation.LogIt;
import gg.neko.spiceit.injector.InjectorUtils;
import gg.neko.spiceit.injector.PatternSolver;
import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import javassist.CannotCompileException;
import javassist.CtField;
import javassist.CtMethod;

/**
 * A {@link LogItInjector} implementation that uses {@link org.slf4j.Logger}.
 */
public class Slf4jLogItInjector implements LogItInjector {

    /**
     * {@inheritDoc}
     */
    @Override
    public void inject(LogIt logIt, CtMethod ctMethod) {
        CtField ctLoggerField = InjectorUtils.getLoggerField(ctMethod.getDeclaringClass());

        Slf4jLogItInjector.logEntry(logIt, ctMethod, ctLoggerField);
        Slf4jLogItInjector.logError(logIt, ctMethod, ctLoggerField);
        Slf4jLogItInjector.logExit(logIt, ctMethod, ctLoggerField);
    }

    private static void logEntry(LogIt logIt, CtMethod ctMethod, CtField ctLoggerField) {
        String solvedPattern = PatternSolver.solve(logIt.entryPattern(), ctMethod);
        String logStatement = InjectorUtils.logPattern(logIt.entryLogLevel(),
                                                       ctLoggerField.getName(),
                                                       solvedPattern);
        try {
            ctMethod.insertBefore(logStatement);
        } catch (CannotCompileException e) {
            throw new SpiceItInjectorException("failed to insert LogIt entry log statement for method " + ctMethod.getLongName(), e);
        }
    }

    private static void logError(LogIt logIt, CtMethod ctMethod, CtField ctLoggerField) {
        String solvedPattern = PatternSolver.solve(logIt.errorPattern(), ctMethod);
        String logStatement = InjectorUtils.logPattern(logIt.errorLogLevel(),
                                                       ctLoggerField.getName(),
                                                       solvedPattern);
        String logAndRethrowBlock = "{"
                                    + logStatement
                                    + System.lineSeparator()
                                    + "throw $e;"
                                    + System.lineSeparator()
                                    + "}";
        try {
            ctMethod.addCatch(logAndRethrowBlock, InjectorUtils.getCatchExceptionTypeName());
        } catch (CannotCompileException e) {
            throw new SpiceItInjectorException("failed to insert LogIt error catch block for method " + ctMethod.getLongName(), e);
        }
    }

    private static void logExit(LogIt logIt, CtMethod ctMethod, CtField ctLoggerField) {
        String solvedPattern = PatternSolver.solve(logIt.exitPattern(), ctMethod);
        String logStatement = InjectorUtils.logPattern(logIt.exitLogLevel(),
                                                       ctLoggerField.getName(),
                                                       solvedPattern);

        try {
            ctMethod.insertAfter(logStatement);
        } catch (CannotCompileException e) {
            throw new SpiceItInjectorException("failed to insert LogIt exit log statement for method " + ctMethod.getLongName(), e);
        }
    }

}
