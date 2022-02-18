package com.seamlesspay.net;

import com.google.gson.*;
import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.InvalidRequestException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.model.*;
import com.seamlesspay.util.StringUtils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;

public abstract class ApiResource extends SPObject {
  public static final Charset CHARSET = StandardCharsets.UTF_8;

  private static ResponseGetter responseGetter = new LiveResponseGetter();

  public static final Gson GSON = createGson();

  public static void setResponseGetter(ResponseGetter srg) {
    ApiResource.responseGetter = srg;
  }

  private static Gson createGson() {
    GsonBuilder builder =
        new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(RawJsonObject.class, new RawJsonObjectDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());

    return builder.create();
  }

  private static String className(Class<?> clazz) {
    // Convert CamelCase to snake_case
    String className = StringUtils.toSnakeCase(clazz.getSimpleName());

    // Handle namespaced resources by checking if the class is in a sub-package, and if so prepend
    // it to the class name
    String[] parts = clazz.getPackage().getName().split("\\.", -1);
    assert parts.length == 3 || parts.length == 4;
    if (parts.length == 4) {
      // The first three parts are always "com.seamlesspay.model", the fourth part is the sub-package
      className = parts[3] + "/" + className;
    }

    // Handle special cases
    switch (className) {
      case "invoice_item":
        return "invoiceitem";
      case "file_upload":
        return "file";
      default:
        return className;
    }
  }

  protected static String singleClassUrl(Class<?> clazz) {
    return singleClassUrl(clazz, SPAPI.getApiBase());
  }

  protected static String singleClassUrl(Class<?> clazz, String apiBase) {
    return String.format("%s/v1/%s", apiBase, className(clazz));
  }

  protected static String classUrl(Class<?> clazz) {
    return classUrl(clazz, SPAPI.getApiBase());
  }

  protected static String classUrl(Class<?> clazz, String apiBase) {
    return String.format("%ss", singleClassUrl(clazz, apiBase));
  }

  protected static String instanceUrl(Class<?> clazz, String id) throws InvalidRequestException {
    return instanceUrl(clazz, id, SPAPI.getApiBase());
  }

  protected static String instanceUrl(Class<?> clazz, String id, String apiBase) {
    return String.format("%s/%s", classUrl(clazz, apiBase), urlEncode(id));
  }

  public enum RequestMethod {
    GET,
    POST,
    PUT,
    DELETE
  }

  /** URL-encodes a string. */
  public static String urlEncode(String str) {
    // Preserve original behavior that passing null for an object id will lead
    // to us actually making a request to /v1/foo/null
    if (str == null) {
      return null;
    }

    try {
      // Don't use strict form encoding by changing the square bracket control
      // characters back to their literals. This is fine by the server, and
      // makes these parameter strings easier to read.
      return URLEncoder.encode(str, CHARSET.name()).replaceAll("%5B", "[").replaceAll("%5D", "]");
    } catch (UnsupportedEncodingException e) {
      // This can literally never happen, and lets us avoid having to catch
      // UnsupportedEncodingException in callers.
      throw new AssertionError("UTF-8 is unknown");
    }
  }

  /** URL-encode a string ID in url path formatting. */
  public static String urlEncodeId(String id) throws InvalidRequestException {
    if (id == null) {
      throw new InvalidRequestException(
          "Invalid null ID found for url path formatting. This can be because your string ID "
              + "argument to the API method is null, or the ID field in your Seamless Pay object "
              + "instance is null.",
          null,
          null,
          0,
          null);
    }

    return urlEncode(id);
  }

  public static <T extends SPObjectInterface> T request(ApiResource.RequestMethod method,
                                                        String url,
                                                        ApiRequestParams params,
                                                        Class<T> clazz,
                                                        RequestOptions options)
    throws SPException {
    checkNullTypedParams(url, params);
    return request(method, url, params.toMap(), clazz, options);
  }

  public static <T extends SPObjectInterface> T request(ApiResource.RequestMethod method,
                                                        String url,
                                                        Map<String, Object> params,
                                                        Class<T> clazz,
                                                        RequestOptions options)
    throws SPException {

    return ApiResource.responseGetter.request(method, url, params, clazz, options);
  }

  public static InputStream requestStream(ApiResource.RequestMethod method,
                                          String url,
                                          ApiRequestParams params,
                                          RequestOptions options)
      throws SPException {

    checkNullTypedParams(url, params);
    return requestStream(method, url, params.toMap(), options);
  }

  public static InputStream requestStream(ApiResource.RequestMethod method,
                                          String url,
                                          Map<String, Object> params,
                                          RequestOptions options)
    throws SPException {

    return ApiResource.responseGetter.requestStream(method, url, params, options);
  }

  public static <T extends SPCollectionInterface<?>> T requestSPCollection(String url,
                                                                           ApiRequestParams params,
                                                                           Class<T> clazz,
                                                                           RequestOptions options)
    throws SPException {

    Map<String, Object> paramsMap = (params == null) ? null : params.toMap();
    return requestSPCollection(url, paramsMap, clazz, options);
  }

  /**
   * Similar to #request, but specific for use with collection types that come from the API (i.e.
   * lists of resources).
   *
   * <p>Collections need a little extra work because we need to plumb request options and params
   * through so that we can iterate to the next page if necessary.
   */
  public static <T extends SPCollectionInterface<?>> T requestCollection(
      String url, Map<String, Object> params, Class<T> clazz, RequestOptions options)
      throws SPException {

    return request(RequestMethod.GET, url, params, clazz, options);
  }

  /**
   * Similar to #request, but specific for use with collection types that come from the API (i.e.
   * lists of resources).
   *
   * <p>Collections need a little extra work because we need to plumb request options and params
   * through so that we can iterate to the next page if necessary.
   */
  public static <T extends SPCollectionInterface<?>> T requestSPCollection(
      String url, Map<String, Object> params, Class<T> clazz, RequestOptions options)
      throws SPException {

    return request(RequestMethod.GET, url, params, clazz, options);
  }

  /**
   * Invalidate null typed parameters.
   *
   * @param url request url associated with the given parameters.
   * @param params typed parameters to check for null value.
   */
  public static void checkNullTypedParams(String url, ApiRequestParams params) {
    if (params == null) {
      throw new IllegalArgumentException(
          String.format(
              "Found null params for %s. "
                  + "Please pass empty params using param builder via `builder().build()` instead.",
              url));
    }
  }

  private static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime();
    }
  }
}
