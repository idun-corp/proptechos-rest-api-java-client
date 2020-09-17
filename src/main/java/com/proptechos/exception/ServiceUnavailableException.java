package com.proptechos.exception;

public class ServiceUnavailableException extends ProptechOsServiceException {

  public ServiceUnavailableException(String message) {
    super(message);
  }

  public ServiceUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }

}
