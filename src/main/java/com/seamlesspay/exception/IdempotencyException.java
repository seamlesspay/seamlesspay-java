package com.seamlesspay.exception;

public class IdempotencyException extends SPException {
  private static final long serialVersionUID = 2L;

  public IdempotencyException(String message, String requestId, Integer code, Integer statusCode) {
    super(message, requestId, code, statusCode);
  }
}
