package com.stripe.exception;

import com.stripe.model.error.SPError;

public class AuthenticationException extends SPException {
  private static final long serialVersionUID = 2L;

  public AuthenticationException(
      String message, String requestId, Integer code, Integer statusCode) {
    super(message, requestId, code, statusCode);
  }

  public AuthenticationException(String message, String requestId, Integer code, Integer statusCode, SPError error) {
    super(message, requestId, code, statusCode, error);
  }

}
