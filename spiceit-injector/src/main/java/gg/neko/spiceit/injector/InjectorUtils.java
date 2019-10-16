package gg.neko.spiceit.injector;

import gg.neko.spiceit.enumeration.LogLevel;
import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class InjectorUtils {

    private InjectorUtils() {
        throw new UnsupportedOperationException("do not instantiate this class");
    }

    public static CtField getLoggerField(CtClass ctClass) {
        return Arrays.stream(ctClass.getDeclaredFields())
                     .filter(ctField -> Logger.class.getName().equals(getCtFieldType(ctField).getName()))
                     .findFirst()
                     .orElseGet(() -> addLoggerField(ctClass));
    }

    public static String getMethodSignature(CtMethod ctMethod) {
        return String.format(Locale.US,
                             "%s%s",
                             ctMethod.getName(),
                             Arrays.stream(getParameterTypes(ctMethod))
                                   .map(CtClass::getName)
                                   .collect(Collectors.joining(", ", "(", ")")));
    }

    public static String logPattern(LogLevel logLevel, String loggerName, String pattern) {
        return loggerName
               + "."
               + logLevel.getMethodName()
               + "("
               + pattern
               + ");";
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
