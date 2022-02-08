package com.stripe.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SPCreditCardVerification {

  /**
   * AVS Verification Code
   */
  @SerializedName("addressLine1")
  private SPCreditCardVerificationCode addressLine1;

  /**
   * AVS Verification Code
   */
  @SerializedName("addressPostalCode")
  private SPCreditCardVerificationCode addressPostalCode;

  /**
   * CVV Verification Code
   */
  @SerializedName("cvv")
  private SPCreditCardVerificationCode cvv;

}
