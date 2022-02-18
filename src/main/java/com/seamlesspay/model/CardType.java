package com.seamlesspay.model;

import com.google.gson.annotations.SerializedName;

public enum CardType {

  @SerializedName("Credit")
  CREDIT,

  @SerializedName("Debit")
  DEBIT,

  @SerializedName("Prepaid")
  PREPAID

}
