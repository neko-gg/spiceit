package gg.neko.spiceit.injector;

import javassist.CtMethod;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides utility methods to resolve placeholders in log patterns.
 */
public class PatternSolver {

    private static final String TIMING_UNAVAILABLE = "\"TIMING_UNAVAILABLE\"";

    private PatternSolver() {
        throw new UnsupportedOperationException("do not instantiate this class");
    }

    /**
     * Resolves all non time related placeholders in a log pattern, without ignoring any argument.
     *
     * @param pattern  the log pattern
     * @param ctMethod the method that placeholders refer to
     * @return the resolved log pattern
     * @see #solve(String, CtMethod, String, int)
     */
    public static String solve(String pattern, CtMethod ctMethod) {
        return solve(pattern, ctMethod, TIMING_UNAVAILABLE, 0);
    }

    /**
     * Resolves placeholders in a log pattern.
     *
     * @param pattern         the log pattern
     * @param ctMethod        the method that placeholders refer to
     * @param timeReplacement the replacement {@code Java} code for {@code ${method.time}}
     * @param argsOffset      how many arguments to ignore (skipped from the beginning of {@code ctMethod} args)
     *                        when resolving arguments related placeholders
     * @return the resolved log pattern
     */
    public static String solve(String pattern, CtMethod ctMethod, String timeReplacement, int argsOffset) {
        String solvedPattern = "\""
                               + pattern.replaceAll(Pattern.quote("\""), Matcher.quoteReplacement("\\\""))
                                        .replaceAll(Pattern.quote("${method.class.name}"), Matcher.quoteReplacement(ctMethod.getDeclaringClass().getName()))
                                        .replaceAll(Pattern.quote("${method.class.simpleName}"), Matcher.quoteReplacement(ctMethod.getDeclaringClass().getSimpleName()))
                                        .replaceAll(Pattern.quote("${method.name}"), Matcher.quoteReplacement(ctMethod.getName()))
                                        .replaceAll(Pattern.quote("${method.signature}"), Matcher.quoteReplacement(InjectorUtils.getMethodSignature(ctMethod)))
                                        .replaceAll(Pattern.quote("${method.longName}"), Matcher.quoteReplacement(ctMethod.getLongName()))
                                        .replaceAll(Pattern.quote("${method.return}"), Matcher.quoteReplacement("\" + java.lang.String.valueOf(($w)$_) + \""))
                                        .replaceAll(Pattern.quote("${method.exception.message}"), Matcher.quoteReplacement("\" + $e.getMessage() + \""))
                                        .replaceAll(Pattern.quote("${method.args}"), Matcher.quoteReplacement("\" + java.util.Arrays.toString(java.util.Arrays.copyOfRange($args, " + argsOffset + ", ($args).length)) + \""))
                                        .replaceAll(Pattern.quote("${method.time}"), Matcher.quoteReplacement("\" + " + timeReplacement + " + \""))
                               + "\"";

        solvedPattern = replaceAll(solvedPattern,
                                   Pattern.compile("\\$\\{method.args\\[(\\d+)]}"),
                                   matcher -> Matcher.quoteReplacement("\" + java.lang.String.valueOf(($w)$" + (Integer.parseInt(matcher.group(1)) + argsOffset) + ") + \""));

        return solvedPattern;
    }

    /**
     * Finds all the matches of a {@link Pattern} in a {@link String},
     * and replaces each one of them by applying a {@link Function}
     *
     * @param input    the input {@link String}
     * @param pattern  the {@link Pattern} to match
     * @param replacer a {@link Function} that returns the replacement {@link String} for a given {@link Matcher}
     * @return the replaced {@link String}
     */
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
