package com.seamlesspay.model;

import com.google.gson.annotations.SerializedName;
import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.net.ApiRequestParams;
import com.seamlesspay.net.ApiResource;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.param.ChargeUpdateParams;
import com.seamlesspay.param.ChargeVoidParams;
import com.seamlesspay.param.RefundCreateParams;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class Refund extends ApiResource {

  public static final String REFUNDS_URL_PATH = "/refunds";

  /**
   * The ID of base transaction. Length <= 31 characters
   */
  @SerializedName("id")
  private String id;

  /**
   * String with 2 decimal places e.g “25.00”. Length <= 12 characters
   */
  @SerializedName("amount")
  private String amount;

  /**
   * Auth Code
   */
  @SerializedName("authCode")
  private String authCode;

  /**
   * Detail Card Product - Visa, MasterCard, American Express, Discover.
   */
  @SerializedName("paymentNetwork")
  private PaymentNetwork paymentNetwork;

  /**
   * Determines the card type (credit, debit, prepaid) and usage (pin, signature etc.).
   */
  @SerializedName("accountType")
  private CardType accountType;

  /**
   * Optional, for JSON object with up to 8 fields and 64 chars limit.
   */
  @SerializedName("metadata")
  String metadata;

  /**
   * IP Address
   */
  @SerializedName("ipAddress")
  private String ipAddress;

  /**
   * The ID of batch. Length <= 31 characters
   */
  @SerializedName("batch")
  private String batch;

  /**
   * Last four of account number
   */
  @SerializedName("lastFour")
  private String lastFour;

  /**
   * Transaction method - "refund"
   */
  @SerializedName("method")
  private TransactionMethod method;

  /**
   * Transaction status
   */
  @SerializedName("status")
  private TransactionStatus status;

  /**
   * Transaction status code
   */
  @SerializedName("statusCode")
  private String statusCode;

  /**
   * Transaction status description
   */
  @SerializedName("statusDescription")
  private String statusDescription;

  /**
   * The payment method (token) from pan-vault. Length string <= 31 characters
   */
  @SerializedName("token")
  private String token;

  /**
   * Transaction date
   */
  @SerializedName("transactionDate")
  private String transactionDate;

  /**
   * Date of create data
   */
  @SerializedName("businessCard")
  private LocalDateTime createdAt;

  /**
   * Date of update data
   */
  @SerializedName("updatedAt")
  private LocalDateTime updatedAt;





  /**
   * Payment type
   */
  @SerializedName("paymentType")
  private String paymentType;

  /**
   * Expiration Date
   */
  @SerializedName("expDate")
  private String expDate;

  /**
   * Order
   */
  @SerializedName("order")
  private Order order;

  /**
   * String with 2 decimal places e.g “25.00”
   */
  @SerializedName("surchargeFeeAmount")
  private String surchargeFeeAmount;

  /**
   * String with 2 decimal places e.g “25.00”
   */
  @SerializedName("tip")
  private String tip;

  @SerializedName("verification")
  private CreditCardVerification verification;


  public static RefundCollection list(RequestOptions options)
      throws SPException {
    String url = String.format("%s%s", SPAPI.getApiBase(), REFUNDS_URL_PATH);
    return ApiResource.requestSPCollection(url, (ApiRequestParams) null, RefundCollection.class, options);
  }

  /** Create a refund. */
  public static Refund create(Map<String, Object> params) throws SPException {
    return create(params, (RequestOptions) null);
  }

  /** Create a refund. */
  public static Refund create(Map<String, Object> params, RequestOptions options)
      throws SPException {
    String url = String.format("%s%s", SPAPI.getApiBase(), REFUNDS_URL_PATH);
    return ApiResource.request(RequestMethod.POST, url, params, Refund.class, options);
  }

  /** Create a refund. */
  public static Refund create(RefundCreateParams params) throws SPException {
    return create(params, (RequestOptions) null);
  }

  /** Create a refund. */
  public static Refund create(RefundCreateParams params, RequestOptions options) throws SPException {
    return create(params.toMap(), options);
  }

  /** Retrieves the details of an existing refund. */
  public static Refund retrieve(String transactionId) throws SPException {
    return retrieve(transactionId, null);
  }

  /** Retrieves the details of an existing refund. */
  public static Refund retrieve(String transactionId, RequestOptions options) throws SPException {
    String url =
        String.format(
            "%s%s",
            SPAPI.getApiBase(), String.format(REFUNDS_URL_PATH + "/%s", ApiResource.urlEncodeId(transactionId)));
    return ApiResource.request(RequestMethod.GET, url, (Map<String, Object>) null, Refund.class, options);
  }

  /**
   * Updates the specified charge by setting the values of the parameters passed. Any parameters not
   * provided will be left unchanged.
   */
  public Refund update(ChargeUpdateParams params) throws SPException {
    return update(params, (RequestOptions) null);
  }

  /**
   * Updates the specified charge by setting the values of the parameters passed. Any parameters not
   * provided will be left unchanged.
   */
  public Refund update(ChargeUpdateParams params, RequestOptions options) throws SPException {
    String url = String.format(
      "%s%s",
      SPAPI.getApiBase(),
      String.format(REFUNDS_URL_PATH + "/%s", ApiResource.urlEncodeId(this.getId())));
    return ApiResource.request(RequestMethod.PUT, url, params, Refund.class, options);
  }

  public Refund voidCharge(ChargeVoidParams params) throws SPException {
    return voidCharge(params, null);
  }

  public Refund voidCharge(ChargeVoidParams params, RequestOptions options) throws SPException {
    String url = String.format(
      "%s%s",
      SPAPI.getApiBase(),
      String.format(REFUNDS_URL_PATH + "/%s", ApiResource.urlEncodeId(this.getId())));
    return ApiResource.request(RequestMethod.DELETE, url, params, Refund.class, options);
  }

  /**
   * Capture the payment of an existing, uncaptured, charge. This is the second half of the two-step
   * payment flow, where first you created a charge with the capture option set to false.
   */
  public Refund capture() throws SPException {
    return capture(null);
  }

  /**
   * Capture the payment of an existing, uncaptured, charge. This is the second half of the two-step
   * payment flow, where first you created a charge with the capture option set to false.
   */
  public Refund capture(RequestOptions options) throws SPException {
    String url = String.format(
      "%s%s/%s",
      SPAPI.getApiBase(),
      REFUNDS_URL_PATH,
      ApiResource.urlEncodeId(this.getId()));
    ChargeUpdateParams params = ChargeUpdateParams.builder().capture(true).build();
    return ApiResource.request(RequestMethod.PUT, url, params, Refund.class, options);
  }

}
