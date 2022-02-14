package com.stripe.model;

import com.google.gson.annotations.SerializedName;

public enum SPTransactionMethod {

  @SerializedName("charge")
  CHARGE,

  @SerializedName("refund")
  REFUND

}
