package com.seamlesspay.exception;

import com.seamlesspay.model.error.SPError;

public class ApiException extends SPException {
  private static final long serialVersionUID = 2L;

  public ApiException(
      String message, String requestId, Integer code, Integer statusCode, Throwable e) {
    super(message, requestId, code, statusCode, e);
  }

  public ApiException(
      String message, String requestId, Integer code, Integer statusCode, SPError error) {
    super(message, requestId, code, statusCode, error);
  }
}
