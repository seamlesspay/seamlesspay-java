package com.stripe.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CreditCardVerification {

  /**
   * AVS Verification Code
   */
  @SerializedName("addressLine1")
  private CreditCardVerificationCode addressLine1;

  /**
   * AVS Verification Code
   */
  @SerializedName("addressPostalCode")
  private CreditCardVerificationCode addressPostalCode;

  /**
   * CVV Verification Code
   */
  @SerializedName("cvv")
  private CreditCardVerificationCode cvv;

}
