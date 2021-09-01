package org.panda_lang.pandomium.util.exceptions;

/**
 * @author Osiris-Team
 */
public class HttpErrorException extends Exception {
    private int httpErrorCode;
    private String httpErrorMessage;

    public HttpErrorException(int httpErrorCode, String httpErrorMessage) {
        this(httpErrorCode, httpErrorMessage, null);
    }

    public HttpErrorException(int httpErrorCode, String httpErrorMessage, String message) {
        super(message);
        this.httpErrorCode = httpErrorCode;
        this.httpErrorMessage = httpErrorMessage;
    }

    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    public void setHttpErrorCode(int httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
    }

    public String getHttpErrorMessage() {
        return httpErrorMessage;
    }

    public void setHttpErrorMessage(String httpErrorMessage) {
        this.httpErrorMessage = httpErrorMessage;
    }
}
