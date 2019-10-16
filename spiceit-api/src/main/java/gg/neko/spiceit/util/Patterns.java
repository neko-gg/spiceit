package gg.neko.spiceit.util;

public class Patterns {

    private Patterns() {
        throw new UnsupportedOperationException("do not instantiate this class");
    }

    public static final String LOGIT_ENTRY_DEFAULT = "[ENTRY] class: ${method.class.name}, method: ${method.longName}, args: ${method.args}";
    public static final String LOGIT_ENTRY_NO_ARGS = "[ENTRY] class: ${method.class.name}, method: ${method.longName}";
    public static final String LOGIT_ENTRY_NO_CLASS = "[ENTRY] method: ${method.longName}, args: ${method.args}";
    public static final String LOGIT_ENTRY_NO_ARGS_NO_CLASS = "[ENTRY] method: ${method.longName}";

    public static final String LOGIT_ERROR_DEFAULT = "[ERROR] class: ${method.class.name}, method: ${method.longName}, args: ${method.args}, exception: ${method.exception.message}";
    public static final String LOGIT_ERROR_NO_ARGS = "[ERROR] class: ${method.class.name}, method: ${method.longName}, exception: ${method.exception.message}";
    public static final String LOGIT_ERROR_NO_CLASS = "[ERROR] method: ${method.longName}, args: ${method.args}, exception: ${method.exception.message}";
    public static final String LOGIT_ERROR_NO_EXCEPTION_MESSAGE = "[ERROR] class: ${method.class.name}, method: ${method.longName}, args: ${method.args}";
    public static final String LOGIT_ERROR_NO_ARGS_NO_CLASS = "[ERROR] method: ${method.longName}, exception: ${method.exception.message}";
    public static final String LOGIT_ERROR_NO_ARGS_NO_EXCEPTION_MESSAGE = "[ERROR] class: ${method.class.name}, method: ${method.longName}";
    public static final String LOGIT_ERROR_NO_CLASS_NO_EXCEPTION_MESSAGE = "[ERROR] method: ${method.longName}, args: ${method.args}";
    public static final String LOGIT_ERROR_NO_ARGS_NO_CLASS_NO_EXCEPTION_MESSAGE = "[ERROR] method: ${method.longName}";

    public static final String LOGIT_EXIT_DEFAULT = "[EXIT] class: ${method.class.name}, method: ${method.longName}, args: ${method.args}, return: ${method.return}";
    public static final String LOGIT_EXIT_NO_ARGS = "[EXIT] class: ${method.class.name}, method: ${method.longName}, return: ${method.return}";
    public static final String LOGIT_EXIT_NO_CLASS = "[EXIT] method: ${method.longName}, args: ${method.args}, return: ${method.return}";
    public static final String LOGIT_EXIT_NO_RETURN = "[EXIT] class: ${method.class.name}, method: ${method.longName}, args: ${method.args}";
    public static final String LOGIT_EXIT_NO_ARGS_NO_CLASS = "[EXIT] method: ${method.longName}, return: ${method.return}";
    public static final String LOGIT_EXIT_NO_ARGS_NO_RETURN = "[EXIT] class: ${method.class.name}, method: ${method.longName}";
    public static final String LOGIT_EXIT_NO_CLASS_NO_RETURN = "[EXIT] method: ${method.longName}, args: ${method.args}";
    public static final String LOGIT_EXIT_NO_ARGS_NO_CLASS_NO_RETURN = "[EXIT] method: ${method.longName}";

}
