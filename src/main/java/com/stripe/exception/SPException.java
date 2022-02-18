package com.stripe.exception;

import com.stripe.model.error.SPError;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class SPException extends Exception {
  private static final long serialVersionUID = 2L;

  private Integer code;
  private String requestId;
  private Integer statusCode;

  @Setter
  private transient SPError error;

  protected SPException(String message, String requestId, Integer code, Integer statusCode) {
    this(message, requestId, code, statusCode, (Throwable) null);
  }

  protected SPException(String message, String requestId, Integer code, Integer statusCode, SPError error) {
    this(message, requestId, code, statusCode, (Throwable) null);
    setError(error);
  }

  /** Constructs a new SeamlessPay exception with the specified details. */
  protected SPException(
      String message, String requestId, Integer code, Integer statusCode, Throwable e) {
    super(message, e);
    this.code = code;
    this.requestId = requestId;
    this.statusCode = statusCode;
  }

  /**
   * Returns a description of the exception, including the HTTP status code and request ID (if
   * applicable).
   *
   * @return a string representation of the exception.
   */
  @Override
  public String getMessage() {
    String additionalInfo = "";
    if (code != null) {
      additionalInfo += "; code: " + code;
    }
    if (requestId != null) {
      additionalInfo += "; request-id: " + requestId;
    }
    return super.getMessage() + additionalInfo;
  }

  /**
   * Returns a description of the user facing exception
   *
   * @return a string representation of the user facing exception.
   */
  public String getUserMessage() {
    return super.getMessage();
  }
}
