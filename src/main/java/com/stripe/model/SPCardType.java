package com.stripe.model;

import com.google.gson.annotations.SerializedName;

public enum SPCardType {

  @SerializedName("Credit")
  CREDIT,

  @SerializedName("Debit")
  DEBIT,

  @SerializedName("Prepaid")
  PREPAID

}
