package gg.neko.spiceit.exception;

public abstract class SpiceItException extends RuntimeException {

    public SpiceItException() { }

    public SpiceItException(String message) {
        super(message);
    }

    public SpiceItException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpiceItException(Throwable cause) {
        super(cause);
    }

}
