package com.seamlesspay.net;

import com.google.gson.JsonSyntaxException;
import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.*;
import com.seamlesspay.model.error.NotAuthenticatedError;
import com.seamlesspay.model.error.NotFoundError;
import com.seamlesspay.model.error.SPError;
import com.seamlesspay.model.error.UnprocessableError;
import com.seamlesspay.model.SPObjectInterface;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
public class LiveResponseGetter implements ResponseGetter {
  private final HttpClient httpClient;

  /**
   * Initializes a new instance of the {@link LiveResponseGetter} class with default
   * parameters.
   */
  public LiveResponseGetter() {
    this(null);
  }

  /**
   * Initializes a new instance of the {@link LiveResponseGetter} class.
   *
   * @param httpClient the HTTP client to use
   */
  public LiveResponseGetter(HttpClient httpClient) {
    this.httpClient = (httpClient != null) ? httpClient : buildDefaultHttpClient();
  }

  @Override
  public <T extends SPObjectInterface> T request(
      ApiResource.RequestMethod method,
      String url,
      Map<String, Object> params,
      Class<T> clazz,
      RequestOptions options)
      throws SPException {

    SPRequest request = new SPRequest(method, url, params, options);
    SPResponse response = httpClient.requestWithRetries(request);

    int responseCode = response.code();
    String responseBody = response.body();
    String requestId = response.requestId();

    if (responseCode < 200 || responseCode >= 300) {
      handleApiError(response);
    }

    T resource = null;
    try {
      resource = ApiResource.GSON.fromJson(responseBody, clazz);
    } catch (JsonSyntaxException e) {
      log.debug("failed to parse response={}", responseBody, e);
      raiseMalformedJsonError(responseBody, responseCode, requestId, e);
    }

    resource.setLastResponse(response);

    return resource;
  }

  @Override
  public InputStream requestStream(
      ApiResource.RequestMethod method,
      String url,
      Map<String, Object> params,
      RequestOptions options)
      throws SPException {

    SPRequest request = new SPRequest(method, url, params, options);
    SPResponseStream responseStream = httpClient.requestStreamWithRetries(request);

    int responseCode = responseStream.code();

    if (responseCode < 200 || responseCode >= 300) {
      SPResponse response;
      try {
        response = responseStream.unstream();
      } catch (IOException e) {
        throw new ApiConnectionException(
            String.format(
                "IOException during API request to SeamlessPay (%s): %s "
                    + "Please check your internet connection and try again",
              SPAPI.getApiBase(), e.getMessage()),
            e);
      }
      handleApiError(response);
    }

    return responseStream.body();
  }

  private static HttpClient buildDefaultHttpClient() {
    return new HttpURLConnectionClient();
  }

  private static void raiseMalformedJsonError(
      String responseBody, int responseCode, String requestId, Throwable e) throws ApiException {
    String details = e == null ? "none" : e.getMessage();
    throw new ApiException(
        String.format(
            "Invalid response object from API: %s. (HTTP response code was %d). Additional details: %s.",
            responseBody, responseCode, details),
        requestId,
        null,
        responseCode,
        e);
  }

  private static void handleApiError(SPResponse response) throws SPException {

    log.debug("handling error from response={}", response);
    switch (response.code()) {
      case 400:
        try {
          SPError error = ApiResource.GSON.fromJson(response.body(), SPError.class);
          log.debug("extracted error object={}", error);
          if ("idempotency_error".equals(error.getClassName())) {
              throw new IdempotencyException(error.getMessage(), response.requestId(), error.getCode(), response.code());
          }

          throw new InvalidRequestException(
                  error.getMessage(),
                  response.requestId(),
                  error.getCode(),
                  response.code(),
                  null);
        } catch (JsonSyntaxException e) {
          raiseMalformedJsonError(response.body(), response.code(), response.requestId(), e);
        }
        break;

      case 401:
        try {
          NotAuthenticatedError error = ApiResource.GSON.fromJson(response.body(), NotAuthenticatedError.class);
          log.debug("extracted error object={}", error);
          String message = String.format("Not authenticated error, message=%s", error.getMessage());
          throw new AuthenticationException(message, response.requestId(), null, response.code(), error);
        } catch (JsonSyntaxException e) {
          raiseMalformedJsonError(response.body(), response.code(), response.requestId(), e);
        }
        break;

      case 404:
        try {
          NotFoundError error = ApiResource.GSON.fromJson(response.body(), NotFoundError.class);
          log.debug("extracted error object={}", error);
          String message = String.format("Not found error, message=%s", error.getMessage());
          throw new ApiException(message, response.requestId(), null, response.code(), error);
        } catch (JsonSyntaxException e) {
          raiseMalformedJsonError(response.body(), response.code(), response.requestId(), e);
        }
        break;

      case 422:
        try {
          UnprocessableError error = ApiResource.GSON.fromJson(response.body(), UnprocessableError.class);
          log.debug("extracted error object={}", error);
          String message = String.format("Unprocessable error, message=%s", error.getMessage());
          throw new ApiException(message, response.requestId(), null, response.code(), error);
        } catch (JsonSyntaxException e) {
          raiseMalformedJsonError(response.body(), response.code(), response.requestId(), e);
        }
        break;

      default:
        try {
          SPError error = ApiResource.GSON.fromJson(response.body(), SPError.class);
          log.debug("extracted error object={}", error);
          throw new ApiException(error.getMessage(), response.requestId(), error.getCode(), response.code(), error);
        } catch (JsonSyntaxException e) {
          raiseMalformedJsonError(response.body(), response.code(), response.requestId(), e);
        }
    }

  }

}
