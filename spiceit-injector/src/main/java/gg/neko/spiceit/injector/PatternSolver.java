package gg.neko.spiceit.injector;

import javassist.CtMethod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternSolver {

    private PatternSolver() {
        throw new UnsupportedOperationException("do not instantiate this class");
    }

    public static String solve(String pattern, CtMethod ctMethod) {
        return solve(pattern, ctMethod, null);
    }

    public static String solve(String pattern, CtMethod ctMethod, Long time) {
        return "\""
               + pattern.replaceAll(Pattern.quote("${method.class.name}"), Matcher.quoteReplacement(ctMethod.getDeclaringClass().getName()))
                        .replaceAll(Pattern.quote("${method.class.simpleName}"), Matcher.quoteReplacement(ctMethod.getDeclaringClass().getSimpleName()))
                        .replaceAll(Pattern.quote("${method.name}"), Matcher.quoteReplacement(ctMethod.getName()))
                        .replaceAll(Pattern.quote("${method.signature}"), Matcher.quoteReplacement(InjectorUtils.getMethodSignature(ctMethod)))
                        .replaceAll(Pattern.quote("${method.longName}"), Matcher.quoteReplacement(ctMethod.getLongName()))
                        .replaceAll(Pattern.quote("${method.return}"), Matcher.quoteReplacement("\" + java.lang.String.valueOf(($w)$_) + \""))
                        .replaceAll(Pattern.quote("${method.exception.message}"), Matcher.quoteReplacement("\" + $e.getMessage() + \""))
                        .replaceAll(Pattern.quote("${method.args}"), Matcher.quoteReplacement("\" + java.util.Arrays.toString($args) + \""))
                        .replaceAll(Pattern.quote("${method.time}"), null == time ? "NOT_TIMED" : String.valueOf(time))
                        .replaceAll("\\$\\{method.args\\[(\\d+)]}", "\" + java.lang.String.valueOf((\\$w)\\$$1) + \"")
               + "\"";
    }

}
