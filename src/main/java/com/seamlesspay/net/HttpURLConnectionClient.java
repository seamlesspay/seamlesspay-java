package com.seamlesspay.net;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.ApiConnectionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.seamlesspay.util.SPLogger;
import lombok.Cleanup;

public class HttpURLConnectionClient extends HttpClient {

  private static final SPLogger log = SPLogger.get(HttpURLConnectionClient.class);

  private static boolean NETWORK_LOGS_ENABLED = false;

  static {
    if (NETWORK_LOGS_ENABLED) {
      ConsoleHandler handler = new ConsoleHandler();
      handler.setLevel(Level.FINEST);
      Logger log = LogManager.getLogManager().getLogger("");
      log.addHandler(handler);
      log.setLevel(Level.FINEST);
    }
  }

  /** Initializes a new instance of the {@link HttpURLConnectionClient}. */
  public HttpURLConnectionClient() {
    super();
  }

  /**
   * Sends the given request to SeamlessPay's API.
   *
   * @param request the request
   * @return the response
   * @throws ApiConnectionException if an error occurs when sending or receiving
   */
  @Override
  public SPResponseStream requestStream(SPRequest request) throws ApiConnectionException {
    try {
      final HttpURLConnection conn = createConnection(request);

      // Calling `getResponseCode()` triggers the request.
      final int responseCode = conn.getResponseCode();

      final HttpHeaders headers = HttpHeaders.of(conn.getHeaderFields());
      log.debug("response: code=%d", responseCode);
      log.trace("response: headers=%s", headers);

      final InputStream responseStream =
          (responseCode >= 200 && responseCode < 300)
              ? conn.getInputStream()
              : conn.getErrorStream();

      return new SPResponseStream(responseCode, headers, responseStream);

    } catch (IOException e) {
      throw new ApiConnectionException(
          String.format(
              "IOException during API request to SeamlessPay (%s): %s "
                  + "Please check your internet connection and try again",
            SPAPI.getApiBase(), e.getMessage()),
          e);
    }
  }

  /**
   * Sends the given request to SeamlessPay's API, and returns a buffered response.
   *
   * @param request the request
   * @return the response
   * @throws ApiConnectionException if an error occurs when sending or receiving
   */
  @Override
  public SPResponse request(SPRequest request) throws ApiConnectionException {
    final SPResponseStream responseStream = requestStream(request);
    try {
      return responseStream.unstream();
    } catch (IOException e) {
      throw new ApiConnectionException(
          String.format(
              "IOException during API request to SeamlessPay (%s): %s "
                  + "Please check your internet connection and try again.",
            SPAPI.getApiBase(), e.getMessage()),
          e);
    }
  }

  static HttpHeaders getHeaders(SPRequest request) {
    Map<String, List<String>> userAgentHeadersMap = new HashMap<>();

    userAgentHeadersMap.put("User-Agent", Arrays.asList(buildUserAgentString()));
    userAgentHeadersMap.put(
        "X-SeamlessPay-Client-User-Agent", Arrays.asList(buildXClientUserAgentString()));

    return request.headers().withAdditionalHeaders(userAgentHeadersMap);
  }

  private static HttpURLConnection createConnection(SPRequest request)
      throws IOException, ApiConnectionException {
    HttpURLConnection conn = null;

    log.debug("request url=%s", request.url());
    if (request.options().getConnectionProxy() != null) {
      conn =
          (HttpURLConnection) request.url().openConnection(request.options().getConnectionProxy());
      Authenticator.setDefault(
          new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
              return request.options().getProxyCredential();
            }
          });
    } else {
      conn = (HttpURLConnection) request.url().openConnection();
    }

    conn.setConnectTimeout(request.options().getConnectTimeout());
    conn.setReadTimeout(request.options().getReadTimeout());
    conn.setUseCaches(false);
    for (Map.Entry<String, List<String>> entry : getHeaders(request).map().entrySet()) {
      String value = String.join(",", entry.getValue());
      log.trace("setting header: name=%s; value=%s", entry.getKey(), value);
      conn.setRequestProperty(entry.getKey(), value);
    }

    conn.setRequestMethod(request.method().name());
    log.debug("request method=%s", request.method().name());

    if (request.content() != null) {
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", request.content().contentType());
      log.debug("setting header: name=%s; value=%s", "Content-Type", request.content().contentType());

      @Cleanup OutputStream output = conn.getOutputStream();
      log.debug("request content=%s", new String(request.content().byteArrayContent(), ApiResource.CHARSET));
      output.write(request.content().byteArrayContent());
    }

    return conn;
  }
}
