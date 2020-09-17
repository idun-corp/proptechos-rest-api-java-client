package com.proptechos.exception;

public class EntityNotFoundException extends ProptechOsServiceException {

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
