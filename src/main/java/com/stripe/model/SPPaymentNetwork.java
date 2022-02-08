package com.stripe.model;

import com.google.gson.annotations.SerializedName;

public enum SPPaymentNetwork {
  @SerializedName("Visa")
  VISA,

  @SerializedName("MasterCard")
  MASTER_CARD,

  @SerializedName("American Express")
  AMERICAN_EXPRESS,

  @SerializedName("Discover")
  DISCOVER
}
