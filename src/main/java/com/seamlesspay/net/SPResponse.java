package com.seamlesspay.net;

import lombok.ToString;

/** A response from SeamlessPay's API, with body represented as a String. */
@ToString
public class SPResponse extends AbstractSPResponse<String> {
  /**
   * Initializes a new instance of the {@link SPResponse} class.
   *
   * @param code the HTTP status code of the response
   * @param headers the HTTP headers of the response
   * @param body the body of the response
   * @throws NullPointerException if {@code headers} or {@code body} is {@code null}
   */
  public SPResponse(int code, HttpHeaders headers, String body) {
    super(code, headers, body);
  }
}
