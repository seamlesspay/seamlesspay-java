package com.seamlesspay.model;

import com.google.gson.annotations.SerializedName;

public enum TransactionMethod {

  @SerializedName("charge")
  CHARGE,

  @SerializedName("refund")
  REFUND

}
