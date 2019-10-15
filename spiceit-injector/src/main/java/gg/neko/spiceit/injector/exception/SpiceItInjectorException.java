package gg.neko.spiceit.injector.exception;

import gg.neko.spiceit.exception.SpiceItException;

public class SpiceItInjectorException extends SpiceItException {

    public SpiceItInjectorException() { }

    public SpiceItInjectorException(String message) {
        super(message);
    }

    public SpiceItInjectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpiceItInjectorException(Throwable cause) {
        super(cause);
    }

}
