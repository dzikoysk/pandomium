package org.panda_lang.pandomium.util.exceptions;

/**
 * @author Osiris-Team
 */
public class WrongJsonTypeException extends Exception {

    public WrongJsonTypeException() {
    }

    public WrongJsonTypeException(String message) {
        super(message);
    }

    public WrongJsonTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongJsonTypeException(Throwable cause) {
        super(cause);
    }

    public WrongJsonTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
