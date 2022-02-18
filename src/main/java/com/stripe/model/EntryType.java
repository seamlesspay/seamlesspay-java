package com.stripe.model;

import com.google.gson.annotations.SerializedName;

public enum EntryType {

  @SerializedName("card_on_file")
  CARD_ON_FILE,

  @SerializedName("keyed")
  KEYED,

  @SerializedName("recurring")
  RECURRING,

  @SerializedName("ecommerce")
  ECOMMERCE

}
