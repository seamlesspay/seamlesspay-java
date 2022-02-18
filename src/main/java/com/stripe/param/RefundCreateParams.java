// File generated from our OpenAPI spec
package com.stripe.param;

import com.google.gson.annotations.SerializedName;
import com.stripe.model.Currency;
import com.stripe.net.ApiRequestParams;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class RefundCreateParams extends ApiRequestParams {

  /**
   * String with 2 decimal places e.g “25.00”. Length <= 12 characters
   */
  @SerializedName("amount")
  String amount;


  @SerializedName("currency")
  Currency currency;

  /**
   * This field contains the merchant name/product/service to be used in lieu of the DBA name.
   */
  @SerializedName("descriptor")
  String descriptor;

  /**
   * Optional, See https://docs.seamlesspay.com/2020-08-01#section/Idempotent-Requests
   */
  @SerializedName("idempotencyKey")
  String idempotencyKey;

  /**
   * Optional, for JSON object with up to 8 fields and 64 chars limit.
   */
  @SerializedName("metadata")
  String metadata;

  /**
   * Required. The ID of base transaction. Length <= 31 characters
   */
  @SerializedName("transactionID")
  String transactionID;

}


