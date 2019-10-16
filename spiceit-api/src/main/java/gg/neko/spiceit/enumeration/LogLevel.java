package gg.neko.spiceit.enumeration;

public enum LogLevel {

    TRACE("trace"),
    DEBUG("debug"),
    INFO("info"),
    WARN("warn"),
    ERROR("error");

    private String methodName;

    LogLevel(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return this.methodName;
    }

}
