package com.g.exception;

public class SSHClientException extends RuntimeException {
    public SSHClientException() {
    }

    public SSHClientException(String message) {
        super(message);
    }

    public SSHClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSHClientException(Throwable cause) {
        super(cause);
    }

    public SSHClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
