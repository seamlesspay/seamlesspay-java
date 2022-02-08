package com.stripe.model;

import com.google.gson.annotations.SerializedName;

public enum SPTransactionStatus {

  @SerializedName("authorized")
  AUTHORIZED,

  @SerializedName("captured")
  CAPTURED,

  @SerializedName("declined")
  DECLINED,

  @SerializedName("error")
  ERROR

}
