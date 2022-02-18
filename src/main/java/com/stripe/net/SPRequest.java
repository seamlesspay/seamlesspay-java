package com.stripe.net;

import com.seamlesspay.SPAPI;
import com.stripe.exception.ApiConnectionException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.SPException;
import com.stripe.util.StringUtils;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

import static com.stripe.net.ApiResource.RequestMethod.POST;

/** A request to Stripe's API. */
@Value
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class SPRequest {
  /** The HTTP method for the request (GET, POST or DELETE). */
  ApiResource.RequestMethod method;

  /**
   * The URL for the request. If this is a GET or DELETE request, the URL also includes the request
   * parameters in its query string.
   */
  URL url;

  /**
   * The body of the request. For POST requests, this will be either a {@code
   * application/x-www-form-urlencoded} or a {@code multipart/form-data} payload. For non-POST
   * requests, this will be {@code null}.
   */
  HttpContent content;

  /**
   * The HTTP headers of the request ({@code Authorization}, {@code Stripe-Version}, {@code
   * Stripe-Account}, {@code Idempotency-Key}...).
   */
  HttpHeaders headers;

  /** The parameters of the request (as an unmodifiable map). */
  Map<String, Object> params;

  /** The special modifiers of the request. */
  RequestOptions options;

  /**
   * Initializes a new instance of the {@link SPRequest} class.
   *
   * @param method the HTTP method
   * @param url the URL of the request
   * @param params the parameters of the request
   * @param options the special modifiers of the request
   * @throws SPException if the request cannot be initialized for any reason
   */
  public SPRequest(
      ApiResource.RequestMethod method,
      String url,
      Map<String, Object> params,
      RequestOptions options)
      throws SPException {
    try {
      this.params = (params != null) ? Collections.unmodifiableMap(params) : null;
      this.options = (options != null) ? options : RequestOptions.getDefault();
      this.method = method;
      this.url = buildURL(method, url, null);   // TODO create separate URL-params
      this.content = buildContent(method, params);
      this.headers = buildHeaders(method, this.options);
    } catch (IOException e) {
      throw new ApiConnectionException(
          String.format(
              "IOException during API request to Stripe (%s): %s "
                  + "Please check your internet connection and try again. If this problem persists,"
                  + "you should check Stripe's service status at https://twitter.com/stripestatus,"
                  + " or let us know at support@stripe.com.",
            SPAPI.getApiBase(), e.getMessage()),
          e);
    }
  }

  /**
   * Returns a new {@link SPRequest} instance with an additional header.
   *
   * @param name the additional header's name
   * @param value the additional header's value
   * @return the new {@link SPRequest} instance
   */
  public SPRequest withAdditionalHeader(String name, String value) {
    return new SPRequest(
        this.method,
        this.url,
        this.content,
        this.headers.withAdditionalHeader(name, value),
        this.params,
        this.options);
  }

  private static URL buildURL(
      ApiResource.RequestMethod method, String spec, Map<String, Object> params)
      throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(spec);

    URL specUrl = new URL(spec);
    String specQueryString = specUrl.getQuery();

    if ((method != POST) && (params != null)) {
      String queryString = FormEncoder.createQueryString(params);

      if (queryString != null && !queryString.isEmpty()) {
        if (specQueryString != null && !specQueryString.isEmpty()) {
          sb.append("&");
        } else {
          sb.append("?");
        }
        sb.append(queryString);
      }
    }

    return new URL(sb.toString());
  }

  private static HttpContent buildContent(
      ApiResource.RequestMethod method, Map<String, Object> params) throws IOException {
    if (params == null) {
      return null;
    }

    return JsonEncoder.createHttpContent(params);
  }

  private static HttpHeaders buildHeaders(ApiResource.RequestMethod method, RequestOptions options)
      throws AuthenticationException {
    Map<String, List<String>> headerMap = new HashMap<String, List<String>>();

    // Accept
    headerMap.put("Accept", Arrays.asList("application/json"));

    // Accept-Charset
    headerMap.put("Accept-Charset", Arrays.asList(ApiResource.CHARSET.name()));

    // Authorization
    String apiKey = options.getApiKey();
    if (apiKey == null || apiKey.isEmpty()) {
      throw new AuthenticationException(
          "No API key provided. Set your API key using `SPAPI.apiKey = \"<API-KEY>\"`. You can "
              + "get API keys from the Seamless Payment Dashboard. See "
              + "https://docs.seamlesspay.com/2020-08-01#section/Authentication for details",
          null,
          null,
          0);
    } else if (StringUtils.containsWhitespace(apiKey)) {
      throw new AuthenticationException(
          "Your API key is invalid, as it contains whitespace.",
          null,
          null,
          0);
    }
    headerMap.put("Authorization", Arrays.asList(String.format("Bearer %s", apiKey)));

    // Stripe-Version
    if (options.getSeamlessPayVersionOverride() != null) {
      headerMap.put("API-Version", Arrays.asList(options.getSeamlessPayVersionOverride()));
    } else if (options.getSeamlessPayVersion() != null) {
      headerMap.put("API-Version", Arrays.asList(options.getSeamlessPayVersion()));
    } else {
      throw new IllegalStateException(
          "Either `SeamlessPayVersion` or `SeamlessPayVersionOverride` value must be set.");
    }

    // Stripe-Account
    if (options.getSeamlessPayAccount() != null) {
      headerMap.put("SeamlessPay-Account", Arrays.asList(options.getSeamlessPayAccount()));
    }

    // Idempotency-Key
    if (options.getIdempotencyKey() != null) {
      headerMap.put("Idempotency-Key", Arrays.asList(options.getIdempotencyKey()));
    } else if (method == POST) {
      headerMap.put("Idempotency-Key", Arrays.asList(UUID.randomUUID().toString()));
    }

    return HttpHeaders.of(headerMap);
  }
}
