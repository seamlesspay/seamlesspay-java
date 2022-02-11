// File generated from our OpenAPI spec
package com.stripe.model;

import com.google.gson.annotations.SerializedName;
import com.seamlesspay.SPAPI;
import com.stripe.exception.StripeException;
import com.stripe.net.ApiRequestParams;
import com.stripe.net.ApiResource;
import com.stripe.net.RequestOptions;
import com.stripe.param.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class SPCharge extends ApiResource {

  public static final String CHARGES_URL_PATH = "/charges";

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
   * Flag determining credit card class
   */
  @SerializedName("businessCard")
  private Boolean businessCard;

  @SerializedName("currency")
  private SPCurrency currency;

  /**
   * Payment type
   */
  @SerializedName("paymentType")
  private String paymentType;

  /**
   * Detail Card Product - Visa, MasterCard, American Express, Discover.
   */
  @SerializedName("paymentNetwork")
  private SPPaymentNetwork paymentNetwork;

  /**
   * Determines the card type (credit, debit, prepaid) and usage (pin, signature etc.).
   */
  @SerializedName("accountType")
  private SPCardType accountType;

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
  private SPOrder order;

  /**
   * Transaction method
   */
  @SerializedName("method")
  private SPTransactionMethod method;

  /**
   * Transaction status
   */
  @SerializedName("status")
  private SPTransactionStatus status;

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
   * String with 2 decimal places e.g “25.00”
   */
  @SerializedName("surchargeFeeAmount")
  private String surchargeFeeAmount;

  /**
   * The payment method (token) from pan-vault. Length string <= 31 characters
   */
  @SerializedName("token")
  private String token;

  /**
   * String with 2 decimal places e.g “25.00”
   */
  @SerializedName("tip")
  private String tip;

  /**
   * Transaction date
   */
  @SerializedName("transactionDate")
  private String transactionDate;

  @SerializedName("verification")
  private SPCreditCardVerification verification;


  public static SPChargeCollection list(RequestOptions options)
      throws StripeException {
    String url = String.format("%s%s", SPAPI.getApiBase(), CHARGES_URL_PATH);
    return ApiResource.requestSPCollection(url, (ApiRequestParams) null, SPChargeCollection.class, options);
  }

  /**
   * To charge a credit card or other payment source, you create a <code>Charge</code> object. If
   * your API key is in test mode, the supplied payment source (e.g., card) won’t actually be
   * charged, although everything else will occur as if in live mode. (Stripe assumes that the
   * charge would have completed successfully).
   */
  public static SPCharge create(Map<String, Object> params) throws StripeException {
    return create(params, (RequestOptions) null);
  }

  /**
   * To charge a credit card or other payment source, you create a <code>Charge</code> object. If
   * your API key is in test mode, the supplied payment source (e.g., card) won’t actually be
   * charged, although everything else will occur as if in live mode. (Stripe assumes that the
   * charge would have completed successfully).
   */
  public static SPCharge create(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url = String.format("%s%s", SPAPI.getApiBase(), CHARGES_URL_PATH);
    return ApiResource.request(RequestMethod.POST, url, params, SPCharge.class, options);
  }

  /**
   * To charge a credit card or other payment source, you create a <code>Charge</code> object. If
   * your API key is in test mode, the supplied payment source (e.g., card) won’t actually be
   * charged, although everything else will occur as if in live mode. (Stripe assumes that the
   * charge would have completed successfully).
   */
  public static SPCharge create(SPChargeCreateParams params) throws StripeException {
    return create(params, (RequestOptions) null);
  }

  /**
   * To charge a credit card or other payment source, you create a <code>Charge</code> object. If
   * your API key is in test mode, the supplied payment source (e.g., card) won’t actually be
   * charged, although everything else will occur as if in live mode. (Stripe assumes that the
   * charge would have completed successfully).
   */
  public static SPCharge create(SPChargeCreateParams params, RequestOptions options)
      throws StripeException {
    // TODO replace with create(params.toMap()) to avoid code-duplication
    String url = String.format("%s%s", SPAPI.getApiBase(), CHARGES_URL_PATH);
    return ApiResource.request(RequestMethod.POST, url, params, SPCharge.class, options);
  }

  /**
   * Retrieves the details of a charge that has previously been created. Supply the transaction ID
   * that was returned from your previous request, and SeamlessPay will return the corresponding charge
   * information. The same information is returned when creating or refunding the charge.
   */
  public static SPCharge retrieve(String transactionId) throws StripeException {
    return retrieve(transactionId, (Map<String, Object>) null, (RequestOptions) null);
  }

  /**
   * Retrieves the details of a charge that has previously been created. Supply the unique transaction ID
   * that was returned from your previous request, and SeamlessPay will return the corresponding charge
   * information. The same information is returned when creating or refunding the charge.
   */
  public static SPCharge retrieve(String transactionId, RequestOptions options) throws StripeException {
    return retrieve(transactionId, (Map<String, Object>) null, options);
  }

  /**
   * Retrieves the details of a charge that has previously been created. Supply the unique transaction ID
   * that was returned from your previous request, and SeamlessPay will return the corresponding charge
   * information. The same information is returned when creating or refunding the charge.
   */
  public static SPCharge retrieve(String transactionId, Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url =
        String.format(
            "%s%s",
            SPAPI.getApiBase(), String.format(CHARGES_URL_PATH + "/%s", ApiResource.urlEncodeId(transactionId)));
    return ApiResource.request(RequestMethod.GET, url, params, SPCharge.class, options);
  }

  /**
   * Retrieves the details of a charge that has previously been created. Supply the unique charge ID
   * that was returned from your previous request, and Stripe will return the corresponding charge
   * information. The same information is returned when creating or refunding the charge.
   */
  public static SPCharge retrieve(String charge, ChargeRetrieveParams params, RequestOptions options)
      throws StripeException {
    String url =
        String.format(
            "%s%s",
            SPAPI.getApiBase(), String.format("/v1/charges/%s", ApiResource.urlEncodeId(charge)));
    return ApiResource.request(RequestMethod.GET, url, params, SPCharge.class, options);
  }

  /**
   * Updates the specified charge by setting the values of the parameters passed. Any parameters not
   * provided will be left unchanged.
   */
  public SPCharge update(SPChargeUpdateParams params) throws StripeException {
    return update(params, (RequestOptions) null);
  }

  /**
   * Updates the specified charge by setting the values of the parameters passed. Any parameters not
   * provided will be left unchanged.
   */
  public SPCharge update(SPChargeUpdateParams params, RequestOptions options) throws StripeException {
    String url = String.format(
      "%s%s",
      SPAPI.getApiBase(),
      String.format(CHARGES_URL_PATH + "/%s", ApiResource.urlEncodeId(this.getId())));
    return ApiResource.request(RequestMethod.PUT, url, params, SPCharge.class, options);
  }

  public SPCharge voidCharge(SPChargeVoidParams params) throws StripeException {
    return voidCharge(params, null);
  }

  public SPCharge voidCharge(SPChargeVoidParams params, RequestOptions options) throws StripeException {
    String url = String.format(
      "%s%s",
      SPAPI.getApiBase(),
      String.format(CHARGES_URL_PATH + "/%s", ApiResource.urlEncodeId(this.getId())));
    return ApiResource.request(RequestMethod.DELETE, url, params, SPCharge.class, options);
  }

  /**
   * Capture the payment of an existing, uncaptured, charge. This is the second half of the two-step
   * payment flow, where first you created a charge with the capture option set to false.
   */
  public SPCharge capture() throws StripeException {
    return capture(null);
  }

  /**
   * Capture the payment of an existing, uncaptured, charge. This is the second half of the two-step
   * payment flow, where first you created a charge with the capture option set to false.
   */
  public SPCharge capture(RequestOptions options) throws StripeException {
    String url = String.format(
      "%s%s/%s",
      SPAPI.getApiBase(),
      CHARGES_URL_PATH,
      ApiResource.urlEncodeId(this.getId()));
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().capture(true).build();
    return ApiResource.request(RequestMethod.PUT, url, params, SPCharge.class, options);
  }

}
