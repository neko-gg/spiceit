package gg.neko.spiceit.injector;

import gg.neko.spiceit.enumeration.LogLevel;
import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Provides utility methods used by {@code SpiceIt} injectors.
 */
public class InjectorUtils {

    private InjectorUtils() {
        throw new UnsupportedOperationException("do not instantiate this class");
    }

    /**
     * Finds and returns a {@link Logger} field declared in a class.
     * If no such field is found, a new one is declared (as {@code private static final}),
     * added to the class, and returned.
     *
     * @param ctClass the class to examine
     * @return a {@link Logger} field declared in {@code ctClass},
     * eventually just declared and added if one was not already present
     */
    public static CtField getLoggerField(CtClass ctClass) {
        return Arrays.stream(ctClass.getDeclaredFields())
                     .filter(ctField -> Logger.class.getName().equals(getCtFieldType(ctField).getName()))
                     .findFirst()
                     .orElseGet(() -> addLoggerField(ctClass));
    }

    /**
     * Calculates the signature of a method.
     * <br>
     * For example, the method signature of {@link String#replaceAll(String, String)} would be:
     * {@code replaceAll(java.lang.String, java.lang.String)}
     *
     * @param ctMethod the {@link CtMethod} of which to calculate the signature
     * @return the signature of {@code ctMethod}
     */
    public static String getMethodSignature(CtMethod ctMethod) {
        return String.format(Locale.US,
                             "%s%s",
                             ctMethod.getName(),
                             Arrays.stream(getParameterTypes(ctMethod))
                                   .map(CtClass::getName)
                                   .collect(Collectors.joining(", ", "(", ")")));
    }

    /**
     * Generates a {@code Java} statement that logs a {@link String} at a specific level.
     *
     * @param logLevel   the level at which to log
     * @param loggerName the name of the {@link Logger} variable to use
     * @param pattern    the {@link String} to log
     * @return a {@link String} containing a {@code Java} statement that uses
     * {@code loggerName} to log {@code pattern} at {@code logLevel}
     */
    public static String logPattern(LogLevel logLevel, String loggerName, String pattern) {
        return loggerName
               + "."
               + logLevel.getMethodName()
               + "("
               + pattern
               + ");";
    }

    public static CtClass getCatchExceptionTypeName() {
        try {
            return ClassPool.getDefault().get(Throwable.class.getName());
        } catch (NotFoundException e) {
            throw new SpiceItInjectorException(e);
        }
    }

    private static CtClass getCtFieldType(CtField ctField) {
        try {
            return ctField.getType();
        } catch (NotFoundException e) {
            throw new SpiceItInjectorException(e);
        }
    }

    private static CtField addLoggerField(CtClass ctClass) {
        try {
            CtField ctLoggerField = CtField.make(getLoggerFieldDeclarationString(ctClass), ctClass);
            ctClass.addField(ctLoggerField);
            return ctLoggerField;
        } catch (CannotCompileException e) {
            throw new SpiceItInjectorException(e);
        }
    }

    private static String getLoggerFieldDeclarationString(CtClass ctClass) {
        return String.format(Locale.US,
                             "private static final org.slf4j.Logger $SPICEIT_LOGGER = org.slf4j.LoggerFactory.getLogger(%s.class);",
                             ctClass.getName());
    }

    private static CtClass[] getParameterTypes(CtMethod ctMethod) {
        try {
            return ctMethod.getParameterTypes();
        } catch (NotFoundException e) {
            throw new SpiceItInjectorException(e);
        }
    }

}
