package com.seamlesspay.net;

import com.seamlesspay.SPAPI;
import lombok.EqualsAndHashCode;

import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Base64;

@EqualsAndHashCode(callSuper = false)
public class RequestOptions {
  private final String apiKey;
  private final String clientId;
  private final String idempotencyKey;
  private final String seamlessPayAccount;

  /** SeamlessPay version always set at {@link SPAPI#API_VERSION}. */
  private final String seamlessPayVersion = SPAPI.API_VERSION;

  /**
   * SeamlessPay version override when made on behalf of others. This can be used when the returned
   * response will not be deserialized into the current classes pinned to {@link SPAPI#VERSION}.
   */
  private final String seamlessPayVersionOverride;

  private final int connectTimeout;
  private final int readTimeout;

  private final int maxNetworkRetries;
  private final Proxy connectionProxy;
  private final PasswordAuthentication proxyCredential;

  public static RequestOptions getDefault() {
    return new RequestOptions(
      SPAPI.apiKey,
      SPAPI.clientId,
      null,
      null,
      null,
      SPAPI.getConnectTimeout(),
      SPAPI.getReadTimeout(),
      SPAPI.getMaxNetworkRetries(),
      SPAPI.getConnectionProxy(),
      SPAPI.getProxyCredential());
  }

  private RequestOptions(
      String apiKey,
      String clientId,
      String idempotencyKey,
      String seamlessPayAccount,
      String seamlessPayVersionOverride,
      int connectTimeout,
      int readTimeout,
      int maxNetworkRetries,
      Proxy connectionProxy,
      PasswordAuthentication proxyCredential) {
    this.apiKey = (apiKey == null) ? null : Base64.getEncoder().encodeToString(apiKey.getBytes(ApiResource.CHARSET));
    this.clientId = clientId;
    this.idempotencyKey = idempotencyKey;
    this.seamlessPayAccount = seamlessPayAccount;
    this.seamlessPayVersionOverride = seamlessPayVersionOverride;
    this.connectTimeout = connectTimeout;
    this.readTimeout = readTimeout;
    this.maxNetworkRetries = maxNetworkRetries;
    this.connectionProxy = connectionProxy;
    this.proxyCredential = proxyCredential;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getClientId() {
    return clientId;
  }

  public String getIdempotencyKey() {
    return idempotencyKey;
  }

  public String getSeamlessPayAccount() {
    return seamlessPayAccount;
  }

  public String getSeamlessPayVersion() {
    return seamlessPayVersion;
  }

  public String getSeamlessPayVersionOverride() {
    return seamlessPayVersionOverride;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public int getMaxNetworkRetries() {
    return maxNetworkRetries;
  }

  public Proxy getConnectionProxy() {
    return connectionProxy;
  }

  public PasswordAuthentication getProxyCredential() {
    return proxyCredential;
  }

  public static RequestOptionsBuilder builder() {
    return new RequestOptionsBuilder();
  }

  /**
   * Convert request options to builder, retaining invariant values for the integration.
   *
   * @return option builder.
   */
  public RequestOptionsBuilder toBuilder() {
    return new RequestOptionsBuilder().setApiKey(this.apiKey).setSeamlessPayAccount(this.seamlessPayAccount);
  }

  public static final class RequestOptionsBuilder {
    private String apiKey;
    private String clientId;
    private String idempotencyKey;
    private String seamlessPayAccount;
    private String seamlessPayVersionOverride;
    private int connectTimeout;
    private int readTimeout;
    private int maxNetworkRetries;
    private Proxy connectionProxy;
    private PasswordAuthentication proxyCredential;

    /**
     * Constructs a request options builder with the global parameters (API key and client ID) as
     * default values.
     */
    public RequestOptionsBuilder() {
      this.apiKey = SPAPI.apiKey;
      this.clientId = SPAPI.clientId;
      this.connectTimeout = SPAPI.getConnectTimeout();
      this.readTimeout = SPAPI.getReadTimeout();
      this.maxNetworkRetries = SPAPI.getMaxNetworkRetries();
      this.connectionProxy = SPAPI.getConnectionProxy();
      this.proxyCredential = SPAPI.getProxyCredential();
    }

    public String getApiKey() {
      return apiKey;
    }

    public RequestOptionsBuilder setApiKey(String apiKey) {
      this.apiKey = normalizeApiKey(apiKey);
      return this;
    }

    public RequestOptionsBuilder clearApiKey() {
      this.apiKey = null;
      return this;
    }

    public String getClientId() {
      return clientId;
    }

    public RequestOptionsBuilder setClientId(String clientId) {
      this.clientId = normalizeClientId(clientId);
      return this;
    }

    public RequestOptionsBuilder clearClientId() {
      this.clientId = null;
      return this;
    }

    public RequestOptionsBuilder setIdempotencyKey(String idempotencyKey) {
      this.idempotencyKey = idempotencyKey;
      return this;
    }

    public int getConnectTimeout() {
      return connectTimeout;
    }

    /**
     * Sets the timeout value that will be used for making new connections to the SeamlessPay API (in
     * milliseconds).
     *
     * @param timeout timeout value in milliseconds
     */
    public RequestOptionsBuilder setConnectTimeout(int timeout) {
      this.connectTimeout = timeout;
      return this;
    }

    public int getReadTimeout() {
      return readTimeout;
    }

    /**
     * Sets the timeout value that will be used when reading data from an established connection to
     * the SeamlessPay API (in milliseconds).
     *
     * <p>Note that this value should be set conservatively because some API requests can take time
     * and a short timeout increases the likelihood of causing a problem in the backend.
     *
     * @param timeout timeout value in milliseconds
     */
    public RequestOptionsBuilder setReadTimeout(int timeout) {
      this.readTimeout = timeout;
      return this;
    }

    public int getMaxNetworkRetries() {
      return maxNetworkRetries;
    }

    /**
     * Sets the maximum number of times the request will be retried in the event of a failure.
     *
     * @param maxNetworkRetries the number of times to retry the request
     */
    public RequestOptionsBuilder setMaxNetworkRetries(int maxNetworkRetries) {
      this.maxNetworkRetries = maxNetworkRetries;
      return this;
    }

    public Proxy getConnectionProxy() {
      return connectionProxy;
    }

    public RequestOptionsBuilder setConnectionProxy(Proxy connectionProxy) {
      this.connectionProxy = connectionProxy;
      return this;
    }

    public PasswordAuthentication getProxyCredential() {
      return proxyCredential;
    }

    public RequestOptionsBuilder setProxyCredential(PasswordAuthentication proxyCredential) {
      this.proxyCredential = proxyCredential;
      return this;
    }

    public RequestOptionsBuilder clearIdempotencyKey() {
      this.idempotencyKey = null;
      return this;
    }

    public String getIdempotencyKey() {
      return this.idempotencyKey;
    }

    public String getSeamlessPayAccount() {
      return this.seamlessPayAccount;
    }

    public RequestOptionsBuilder setSeamlessPayAccount(String seamlessPayAccount) {
      this.seamlessPayAccount = seamlessPayAccount;
      return this;
    }

    public RequestOptionsBuilder clearAccount() {
      return setSeamlessPayAccount(null);
    }

    public String getSeamlessPayVersionOverride() {
      return this.seamlessPayVersionOverride;
    }

    /**
     * Do not use this except for in API where JSON response is not fully deserialized into explicit
     * SeamlessPay classes, but only passed to other clients as raw data -- essentially making request on
     * behalf of others with their API version. Setting this value in a typical
     * scenario will result in deserialization error as the model classes have schema according to
     * the pinned {@link SPAPI#API_VERSION}
     *
     * @param seamlessPayVersionOverride SeamlessPay version override which belongs to the client to make
     *     request on behalf of.
     * @return option builder
     */
    public RequestOptionsBuilder setSeamlessPayVersionOverride(String seamlessPayVersionOverride) {
      this.seamlessPayVersionOverride = normalizeVersion(seamlessPayVersionOverride);
      return this;
    }

    public RequestOptionsBuilder clearVersionOverride() {
      return setSeamlessPayVersionOverride(null);
    }

    /** Constructs a {@link RequestOptions} with the specified values. */
    public RequestOptions build() {
      return new RequestOptions(
          normalizeApiKey(this.apiKey),
          normalizeClientId(this.clientId),
          normalizeIdempotencyKey(this.idempotencyKey),
          normalizeAccount(this.seamlessPayAccount),
          normalizeVersion(this.seamlessPayVersionOverride),
          connectTimeout,
          readTimeout,
          maxNetworkRetries,
          connectionProxy,
          proxyCredential);
    }
  }

  private static String normalizeApiKey(String apiKey) {
    // null apiKeys are considered "valid"
    if (apiKey == null) {
      return null;
    }
    String normalized = apiKey.trim();
    if (normalized.isEmpty()) {
      throw new InvalidRequestOptionsException("Empty API key specified!");
    }
    return normalized;
  }

  private static String normalizeClientId(String clientId) {
    // null client_ids are considered "valid"
    if (clientId == null) {
      return null;
    }
    String normalized = clientId.trim();
    if (normalized.isEmpty()) {
      throw new InvalidRequestOptionsException("Empty client_id specified!");
    }
    return normalized;
  }

  private static String normalizeVersion(String version) {
    // null versions are considered "valid" and use SPAPI.apiVersion
    if (version == null) {
      return null;
    }
    String normalized = version.trim();
    if (normalized.isEmpty()) {
      throw new InvalidRequestOptionsException("Empty SeamlessPay version specified!");
    }
    return normalized;
  }

  private static String normalizeIdempotencyKey(String idempotencyKey) {
    if (idempotencyKey == null) {
      return null;
    }
    String normalized = idempotencyKey.trim();
    if (normalized.isEmpty()) {
      throw new InvalidRequestOptionsException("Empty Idempotency Key Specified!");
    }
    if (normalized.length() > 255) {
      throw new InvalidRequestOptionsException(
          String.format(
              "Idempotency Key length was %d, which is larger than the 255 character " + "maximum!",
              normalized.length()));
    }
    return normalized;
  }

  private static String normalizeAccount(String seamlessPayAccount) {
    if (seamlessPayAccount == null) {
      return null;
    }
    String normalized = seamlessPayAccount.trim();
    if (normalized.isEmpty()) {
      throw new InvalidRequestOptionsException("Empty SeamlessPay account specified!");
    }
    return normalized;
  }

  public static class InvalidRequestOptionsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidRequestOptionsException(String message) {
      super(message);
    }
  }
}
