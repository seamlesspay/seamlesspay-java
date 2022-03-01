package com.seamlesspay.net;

import com.seamlesspay.util.SPLogger;
import com.seamlesspay.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

public class SPResponseStream extends AbstractSPResponse<InputStream> {

  private static final SPLogger log = SPLogger.get(SPResponseStream.class);

  /**
   * Initializes a new instance of the {@link SPResponseStream} class.
   *
   * @param code the HTTP status code of the response
   * @param headers the HTTP headers of the response
   * @param body streaming body response
   * @throws NullPointerException if {@code headers} or {@code body} is {@code null}
   */
  public SPResponseStream(int code, HttpHeaders headers, InputStream body) {
    super(code, headers, body);
  }

  /**
   * Buffers the entire response body into a string, constructing the appropriate SPResponse
   *
   * @return the SPResponse
   */
  SPResponse unstream() throws IOException {
    final String bodyString = StreamUtils.readToEnd(this.body, ApiResource.CHARSET);
    log.debug("response body=%s", bodyString);
    this.body.close();
    return new SPResponse(this.code, this.headers, bodyString);
  }
}
