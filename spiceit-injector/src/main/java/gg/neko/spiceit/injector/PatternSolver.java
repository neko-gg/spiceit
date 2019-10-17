package gg.neko.spiceit.injector;

import javassist.CtMethod;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternSolver {

    private PatternSolver() {
        throw new UnsupportedOperationException("do not instantiate this class");
    }

    public static String solve(String pattern, CtMethod ctMethod) {
        return solve(pattern, ctMethod, null, 0);
    }

    public static String solve(String pattern, CtMethod ctMethod, String startTimeVariableName, int argsOffset) {
        String solvedPattern = "\""
                               + pattern.replaceAll(Pattern.quote("${method.class.name}"), Matcher.quoteReplacement(ctMethod.getDeclaringClass().getName()))
                                        .replaceAll(Pattern.quote("${method.class.simpleName}"), Matcher.quoteReplacement(ctMethod.getDeclaringClass().getSimpleName()))
                                        .replaceAll(Pattern.quote("${method.name}"), Matcher.quoteReplacement(ctMethod.getName()))
                                        .replaceAll(Pattern.quote("${method.signature}"), Matcher.quoteReplacement(InjectorUtils.getMethodSignature(ctMethod)))
                                        .replaceAll(Pattern.quote("${method.longName}"), Matcher.quoteReplacement(ctMethod.getLongName()))
                                        .replaceAll(Pattern.quote("${method.return}"), Matcher.quoteReplacement("\" + java.lang.String.valueOf(($w)$_) + \""))
                                        .replaceAll(Pattern.quote("${method.exception.message}"), Matcher.quoteReplacement("\" + $e.getMessage() + \""))
                                        .replaceAll(Pattern.quote("${method.time}"), null == startTimeVariableName ? "NOT_TIMED" : Matcher.quoteReplacement("\" + (java.lang.System.currentTimeMillis() - " + startTimeVariableName + ") + \""))
                               + "\"";

        solvedPattern = replaceAll(solvedPattern,
                                   Pattern.compile(Pattern.quote("${method.args}")),
                                   matcher -> Matcher.quoteReplacement("\" + java.util.Arrays.toString(java.util.Arrays.copyOfRange($args, " + argsOffset + ", ($args).length)) + \""));

        solvedPattern = replaceAll(solvedPattern,
                                   Pattern.compile("\\$\\{method.args\\[(\\d+)]}"),
                                   matcher -> Matcher.quoteReplacement("\" + java.lang.String.valueOf(($w)$" + (Integer.parseInt(matcher.group(1)) + argsOffset) + ") + \""));

        return solvedPattern;
    }

    private static String replaceAll(String input, Pattern pattern, Function<Matcher, String> replacer) {
        StringBuffer stringBuffer = new StringBuffer();
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, replacer.apply(matcher));
        }
        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

}
