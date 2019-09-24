package gg.neko.spiceit.injector.exception;

import gg.neko.spiceit.exception.SpiceItException;

public class SpiceItReviserException extends SpiceItException {

    public SpiceItReviserException() { }

    public SpiceItReviserException(String message) {
        super(message);
    }

    public SpiceItReviserException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpiceItReviserException(Throwable cause) {
        super(cause);
    }

}
