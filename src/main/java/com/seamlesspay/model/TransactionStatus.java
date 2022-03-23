package com.seamlesspay.model;

import com.google.gson.annotations.SerializedName;

public enum TransactionStatus {

  @SerializedName("authorized")
  AUTHORIZED,

  @SerializedName("captured")
  CAPTURED,

  @SerializedName("settled")
  SETTLED,

  @SerializedName("declined")
  DECLINED,

  @SerializedName("error")
  ERROR

}
