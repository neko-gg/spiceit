package gg.neko.spiceit.injector;

import gg.neko.spiceit.annotation.LogIt;
import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class LogItInjector {

    private LogItInjector() {
        throw new UnsupportedOperationException("do not instantiate this class");
    }

    public static void inject(LogIt logIt, CtMethod ctMethod) {
        CtField ctLoggerField = InjectorUtils.getLoggerField(ctMethod.getDeclaringClass());

        logEntry(logIt, ctMethod, ctLoggerField);
        logError(logIt, ctMethod, ctLoggerField);
        logExit(logIt, ctMethod, ctLoggerField);
    }

    private static void logEntry(LogIt logIt, CtMethod ctMethod, CtField ctLoggerField) {
        String solvedPattern = PatternSolver.solve(logIt.entryPattern(), ctMethod);
        String logStatement = InjectorUtils.logPattern(logIt.entryLogLevel(),
                                                       ctLoggerField.getName(),
                                                       solvedPattern);
        try {
            ctMethod.insertBefore(logStatement);
        } catch (CannotCompileException e) {
            throw new SpiceItInjectorException(e);
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
            ctMethod.addCatch(logAndRethrowBlock, getCatchExceptionTypeName());
        } catch (CannotCompileException e) {
            throw new SpiceItInjectorException(e);
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
            throw new SpiceItInjectorException(e);
        }
    }

    private static CtClass getCatchExceptionTypeName() {
        try {
            return ClassPool.getDefault().get(Throwable.class.getName());
        } catch (NotFoundException e) {
            throw new SpiceItInjectorException(e);
        }
    }

}
