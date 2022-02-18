package com.seamlesspay.exception;

public class InvalidRequestException extends SPException {
  private static final long serialVersionUID = 2L;

  public InvalidRequestException(
      String message,
      String requestId,
      Integer code,
      Integer statusCode,
      Throwable e) {
    super(message, requestId, code, statusCode, e);
  }
}
