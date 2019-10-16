package gg.neko.spiceit.util;

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

}
