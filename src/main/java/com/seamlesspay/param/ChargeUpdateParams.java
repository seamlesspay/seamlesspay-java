// File generated from our OpenAPI spec
package com.seamlesspay.param;

import com.google.gson.annotations.SerializedName;
import com.seamlesspay.model.Order;
import com.seamlesspay.model.TransactionMethod;
import com.seamlesspay.net.ApiRequestParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChargeUpdateParams extends ApiRequestParams {

  /**
   * String with 2 decimal places e.g “25.00”. Length {@literal <}= 12 characters
   */
  @SerializedName("amount")
  String amount;

  /**
   * Transaction method
   */
  @SerializedName("method")
  private TransactionMethod method;

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
   * Surcharge fee amount. Length {@literal <}= 12 characters
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
  Order order;

  /**
   * See https://docs.seamlesspay.com/2020-08-01#section/Idempotent-Requests
   */
  @SerializedName("idempotencyKey")
  String idempotencyKey;

}
