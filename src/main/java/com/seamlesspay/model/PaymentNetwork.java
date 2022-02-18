package com.seamlesspay.model;

import com.google.gson.annotations.SerializedName;

public enum PaymentNetwork {
  @SerializedName("Visa")
  VISA,

  @SerializedName("MasterCard")
  MASTER_CARD,

  @SerializedName("American Express")
  AMERICAN_EXPRESS,

  @SerializedName("Discover")
  DISCOVER
}
