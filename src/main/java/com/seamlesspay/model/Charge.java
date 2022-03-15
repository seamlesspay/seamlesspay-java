package com.seamlesspay.model;

import com.google.gson.annotations.SerializedName;
import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.net.ApiRequestParams;
import com.seamlesspay.net.ApiResource;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.param.ChargeCreateParams;
import com.seamlesspay.param.ChargeUpdateParams;
import com.seamlesspay.param.ChargeVoidParams;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Charge extends ApiResource {

  public static final String CHARGES_URL_PATH = "/charges";

  /**
   * The ID of base transaction. Length {@literal <}= 31 characters
   */
  @SerializedName("id")
  private String id;

  /**
   * String with 2 decimal places e.g "25.00". Length {@literal <}= 12 characters
   */
  @SerializedName("amount")
  private String amount;

  /**
   * Auth Code
   */
  @SerializedName("authCode")
  private String authCode;

  /**
   * Flag determining credit card class
   */
  @SerializedName("businessCard")
  private Boolean businessCard;

  @SerializedName("currency")
  private Currency currency;

  /**
   * Payment type
   */
  @SerializedName("paymentType")
  private String paymentType;

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
   * Expiration Date
   */
  @SerializedName("expDate")
  private String expDate;

  /**
   * IP Address
   */
  @SerializedName("ipAddress")
  private String ipAddress;

  /**
   * Last four of account number
   */
  @SerializedName("lastFour")
  private String lastFour;

  /**
   * Order
   */
  @SerializedName("order")
  private Order order;

  /**
   * Transaction method
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
   * String with 2 decimal places e.g "25.00"
   */
  @SerializedName("surchargeFeeAmount")
  private String surchargeFeeAmount;

  /**
   * The payment method (token) from pan-vault. Length string {@literal <}= 31 characters
   */
  @SerializedName("token")
  private String token;

  /**
   * String with 2 decimal places e.g "25.00"
   */
  @SerializedName("tip")
  private String tip;

  /**
   * Transaction date
   */
  @SerializedName("transactionDate")
  private String transactionDate;

  @SerializedName("verification")
  private CreditCardVerification verification;

  @SerializedName("batch")
  private String batch;


  public static ChargeCollection list() throws SPException {
    return list(null);
  }

  public static ChargeCollection list(RequestOptions options) throws SPException {
    String url = String.format("%s%s", SPAPI.getApiBase(), CHARGES_URL_PATH);
    return ApiResource.requestSPCollection(url, (ApiRequestParams) null, ChargeCollection.class, options);
  }

  /**
   * To charge a credit card or other payment source, you create a <code>Charge</code> object. If
   * your API key is in test mode, the supplied payment source (e.g., card) won't actually be
   * charged, although everything else will occur as if in live mode. (SeamlessPay assumes that the
   * charge would have completed successfully).
   */
  public static Charge create(Map<String, Object> params) throws SPException {
    return create(params, (RequestOptions) null);
  }

  /**
   * To charge a credit card or other payment source, you create a <code>Charge</code> object. If
   * your API key is in test mode, the supplied payment source (e.g., card) won't actually be
   * charged, although everything else will occur as if in live mode. (SeamlessPay assumes that the
   * charge would have completed successfully).
   */
  public static Charge create(Map<String, Object> params, RequestOptions options)
      throws SPException {
    String url = String.format("%s%s", SPAPI.getApiBase(), CHARGES_URL_PATH);
    return ApiResource.request(RequestMethod.POST, url, params, Charge.class, options);
  }

  /**
   * To charge a credit card or other payment source, you create a <code>Charge</code> object. If
   * your API key is in test mode, the supplied payment source (e.g., card) won't actually be
   * charged, although everything else will occur as if in live mode. (SeamlessPay assumes that the
   * charge would have completed successfully).
   */
  public static Charge create(ChargeCreateParams params) throws SPException {
    return create(params, (RequestOptions) null);
  }

  /**
   * To charge a credit card or other payment source, you create a <code>Charge</code> object. If
   * your API key is in test mode, the supplied payment source (e.g., card) won't actually be
   * charged, although everything else will occur as if in live mode. (SeamlessPay assumes that the
   * charge would have completed successfully).
   */
  public static Charge create(ChargeCreateParams params, RequestOptions options) throws SPException {
    return create(params.toMap(), options);
  }

  /**
   * Retrieves the details of a charge that has previously been created. Supply the transaction ID
   * that was returned from your previous request, and SeamlessPay will return the corresponding charge
   * information. The same information is returned when creating or refunding the charge.
   */
  public static Charge retrieve(String transactionId) throws SPException {
    return retrieve(transactionId, null);
  }

  /**
   * Retrieves the details of a charge that has previously been created. Supply the unique transaction ID
   * that was returned from your previous request, and SeamlessPay will return the corresponding charge
   * information. The same information is returned when creating or refunding the charge.
   */
  public static Charge retrieve(String transactionId, RequestOptions options) throws SPException {
    String url = String.format(
      "%s%s",
      SPAPI.getApiBase(),
      String.format(CHARGES_URL_PATH + "/%s", ApiResource.urlEncodeId(transactionId)));
    return ApiResource.request(RequestMethod.GET, url, (Map<String, Object>) null, Charge.class, options);
  }

  /**
   * Updates the specified charge by setting the values of the parameters passed. Any parameters not
   * provided will be left unchanged.
   */
  public Charge update(ChargeUpdateParams params) throws SPException {
    return update(params, (RequestOptions) null);
  }

  /**
   * Updates the specified charge by setting the values of the parameters passed. Any parameters not
   * provided will be left unchanged.
   */
  public Charge update(ChargeUpdateParams params, RequestOptions options) throws SPException {
    String url = String.format(
      "%s%s",
      SPAPI.getApiBase(),
      String.format(CHARGES_URL_PATH + "/%s", ApiResource.urlEncodeId(this.getId())));
    return ApiResource.request(RequestMethod.PUT, url, params, Charge.class, options);
  }

  public Charge voidCharge(ChargeVoidParams params) throws SPException {
    return voidCharge(params, null);
  }

  public Charge voidCharge(ChargeVoidParams params, RequestOptions options) throws SPException {
    String url = String.format(
      "%s%s",
      SPAPI.getApiBase(),
      String.format(CHARGES_URL_PATH + "/%s", ApiResource.urlEncodeId(this.getId())));
    return ApiResource.request(RequestMethod.DELETE, url, params, Charge.class, options);
  }

  /**
   * Capture the payment of an existing, uncaptured, charge. This is the second half of the two-step
   * payment flow, where first you created a charge with the capture option set to false.
   */
  public Charge capture() throws SPException {
    return capture(null);
  }

  /**
   * Capture the payment of an existing, uncaptured, charge. This is the second half of the two-step
   * payment flow, where first you created a charge with the capture option set to false.
   */
  public Charge capture(RequestOptions options) throws SPException {
    String url = String.format(
      "%s%s/%s",
      SPAPI.getApiBase(),
      CHARGES_URL_PATH,
      ApiResource.urlEncodeId(this.getId()));
    ChargeUpdateParams params = ChargeUpdateParams.builder()
      .capture(true)
      .amount(getAmount())
      .build();
    return ApiResource.request(RequestMethod.PUT, url, params, Charge.class, options);
  }

}
