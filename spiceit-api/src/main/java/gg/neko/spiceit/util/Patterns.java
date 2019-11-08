package gg.neko.spiceit.util;

import gg.neko.spiceit.annotation.LogIt;

/**
 * A collection of common log patterns.
 * <p>
 * Available placeholders are:
 * <table summary="Placeholders with their substitution and examples.">
 *   <tr>
 *     <td><center><i>placeholder</i></center></td>
 *     <td><center><i>substitution</i></center></td>
 *     <td><center><i>example</i></center></td>
 *   </tr>
 *   <tr>
 *     <td>{@code ${method.class.name}}</td>
 *     <td>the method declaring class name</td>
 *     <td>{@code java.lang.String}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ${method.class.simpleName}}</td>
 *     <td>the method declaring class simple name</td>
 *     <td>{@code String}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ${method.name}}</td>
 *     <td>the method name</td>
 *     <td>{@code replaceAll}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ${method.signature}}</td>
 *     <td>the method signature</td>
 *     <td>{@code replaceAll(java.lang.String, java.lang.String)}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ${method.longName}}</td>
 *     <td>the method long name</td>
 *     <td>{@code java.lang.String.replaceAll(java.lang.String,java.lang.String)}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ${method.return}}</td>
 *     <td>the method return value</td>
 *     <td>{@code the pencil is on the table}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ${method.exception.message}}</td>
 *     <td>the method exception message</td>
 *     <td>{@code String index out of range: 420}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ${method.args}}</td>
 *     <td>the method arguments</td>
 *     <td>{@code [nyan, cat]}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ${method.args[$index]}}, e.g.: {@code ${method.args[1]}}</td>
 *     <td>the {@code $index}-th method argument, starting at 1</td>
 *     <td>{@code nyan}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ${method.time}}</td>
 *     <td>the method execution time in milliseconds</td>
 *     <td>{@code 69}</td>
 *   </tr>
 * </table>
 * <p>
 * Note that not all placeholders are available at all times, e.g.: {@code ${method.exception.message}} is only available in {@link LogIt#errorPattern()}.
 */
public class Patterns {

    private Patterns() {
        throw new UnsupportedOperationException("do not instantiate this class");
    }

    public static final String LOGIT_ENTRY_DEFAULT = "[ENTRY] class: ${method.class.name}, method: ${method.signature}, args: ${method.args}";
    public static final String LOGIT_ENTRY_NO_ARGS = "[ENTRY] class: ${method.class.name}, method: ${method.signature}";
    public static final String LOGIT_ENTRY_NO_CLASS = "[ENTRY] method: ${method.signature}, args: ${method.args}";
    public static final String LOGIT_ENTRY_NO_ARGS_NO_CLASS = "[ENTRY] method: ${method.signature}";

    public static final String LOGIT_ERROR_DEFAULT = "[ERROR] class: ${method.class.name}, method: ${method.signature}, args: ${method.args}, exception: ${method.exception.message}";
    public static final String LOGIT_ERROR_NO_ARGS = "[ERROR] class: ${method.class.name}, method: ${method.signature}, exception: ${method.exception.message}";
    public static final String LOGIT_ERROR_NO_CLASS = "[ERROR] method: ${method.signature}, args: ${method.args}, exception: ${method.exception.message}";
    public static final String LOGIT_ERROR_NO_EXCEPTION_MESSAGE = "[ERROR] class: ${method.class.name}, method: ${method.signature}, args: ${method.args}";
    public static final String LOGIT_ERROR_NO_ARGS_NO_CLASS = "[ERROR] method: ${method.signature}, exception: ${method.exception.message}";
    public static final String LOGIT_ERROR_NO_ARGS_NO_EXCEPTION_MESSAGE = "[ERROR] class: ${method.class.name}, method: ${method.signature}";
    public static final String LOGIT_ERROR_NO_CLASS_NO_EXCEPTION_MESSAGE = "[ERROR] method: ${method.signature}, args: ${method.args}";
    public static final String LOGIT_ERROR_NO_ARGS_NO_CLASS_NO_EXCEPTION_MESSAGE = "[ERROR] method: ${method.signature}";

    public static final String LOGIT_EXIT_DEFAULT = "[EXIT] class: ${method.class.name}, method: ${method.signature}, args: ${method.args}, return: ${method.return}";
    public static final String LOGIT_EXIT_NO_ARGS = "[EXIT] class: ${method.class.name}, method: ${method.signature}, return: ${method.return}";
    public static final String LOGIT_EXIT_NO_CLASS = "[EXIT] method: ${method.signature}, args: ${method.args}, return: ${method.return}";
    public static final String LOGIT_EXIT_NO_RETURN = "[EXIT] class: ${method.class.name}, method: ${method.signature}, args: ${method.args}";
    public static final String LOGIT_EXIT_NO_ARGS_NO_CLASS = "[EXIT] method: ${method.signature}, return: ${method.return}";
    public static final String LOGIT_EXIT_NO_ARGS_NO_RETURN = "[EXIT] class: ${method.class.name}, method: ${method.signature}";
    public static final String LOGIT_EXIT_NO_CLASS_NO_RETURN = "[EXIT] method: ${method.signature}, args: ${method.args}";
    public static final String LOGIT_EXIT_NO_ARGS_NO_CLASS_NO_RETURN = "[EXIT] method: ${method.signature}";

    public static final String TIMEIT_DEFAULT = "[TIME] [${method.time}ms] class: ${method.class.name}, method: ${method.signature}";
    public static final String TIMEIT_WITH_ARGS = "[TIME] [${method.time}ms] class: ${method.class.name}, method: ${method.signature}, args: ${method.args}";
    public static final String TIMEIT_WITH_RETURN = "[TIME] [${method.time}ms] class: ${method.class.name}, method: ${method.signature}, return: ${method.return}";
    public static final String TIMEIT_WITH_ARGS_WITH_RETURN = "[TIME] [${method.time}ms] class: ${method.class.name}, method: ${method.signature}, args: ${method.args}, return: ${method.return}";
    public static final String TIMEIT_NO_CLASS = "[TIME] [${method.time}ms] method: ${method.signature}";
    public static final String TIMEIT_NO_CLASS_WITH_ARGS = "[TIME] [${method.time}ms] method: ${method.signature}, args: ${method.args}";
    public static final String TIMEIT_NO_CLASS_WITH_RETURN = "[TIME] [${method.time}ms] method: ${method.signature}, return: ${method.return}";
    public static final String TIMEIT_NO_CLASS_WITH_ARGS_WITH_RETURN = "[TIME] [${method.time}ms] method: ${method.signature}, args: ${method.args}, return: ${method.return}";

}
