package com.seamlesspay.param;

import com.google.gson.annotations.SerializedName;
import com.seamlesspay.net.ApiRequestParams;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ChargeVoidParams extends ApiRequestParams {

  /**
   * The ID of base transaction. Length {@literal <}= 31 characters
   */
  @SerializedName("transactionID")
  String transactionId;

  /**
   * See https://docs.seamlesspay.com/2020-08-01#section/Idempotent-Requests
   */
  @SerializedName("idempotencyKey")
  String idempotencyKey;

}
