package com.proptechos.exception;

public class ServiceInvalidUsageException extends ProptechOsServiceException {

    public ServiceInvalidUsageException(String message) {
        super(message);
    }

    public ServiceInvalidUsageException(String message, Throwable cause) {
        super(message, cause);
    }

}
