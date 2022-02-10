// File generated from our OpenAPI spec
package com.stripe.param;

import com.google.gson.annotations.SerializedName;
import com.stripe.model.SPOrder;
import com.stripe.net.ApiRequestParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SPChargeUpdateParams extends ApiRequestParams {

  /**
   * String with 2 decimal places e.g “25.00”. Length <= 12 characters
   */
  @SerializedName("amount")
  String amount;

  /**
   * String with 2 decimal places e.g “25.00”.
   */
  @SerializedName("tip")
  String tip;

  /**
   * String with 2 decimal places e.g “25.00”.
   */
  @SerializedName("taxAmount")
  String taxAmount;

  /**
   * Surcharge fee amount. Length <= 12 characters
   */
  @SerializedName("surchargeFeeAmount")
  String surchargeFeeAmount;

  /**
   * Create a capture transaction.
   */
  @SerializedName("capture")
  Boolean capture;

  /**
   */
  @SerializedName("order")
  SPOrder order;

  /**
   * See https://docs.seamlesspay.com/2020-08-01#section/Idempotent-Requests
   */
  @SerializedName("idempotencyKey")
  String idempotencyKey;

}
