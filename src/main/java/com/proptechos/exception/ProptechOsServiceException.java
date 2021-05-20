package com.proptechos.exception;

public class ProptechOsServiceException extends RuntimeException {

    public ProptechOsServiceException(String message) {
        super(message);
    }

    public ProptechOsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
