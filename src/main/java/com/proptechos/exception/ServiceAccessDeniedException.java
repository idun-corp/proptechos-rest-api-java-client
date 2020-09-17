package com.proptechos.exception;

public class ServiceAccessDeniedException extends ProptechOsServiceException {

  public ServiceAccessDeniedException(String message) {
    super(message);
  }

  public ServiceAccessDeniedException(String message, Throwable cause) {
    super(message, cause);
  }

}
