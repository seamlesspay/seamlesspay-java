package com.stripe.model;

import com.google.gson.annotations.SerializedName;

public enum SPCreditCardVerificationCode {

  /**
   * Verification passed
   */
  @SerializedName("pass")
  PASS,

  /**
   * Verification failed
   */
  @SerializedName("fail")
  FAIL,

  /**
   * No verification was performed
   */
  @SerializedName("unchecked")
  UNCHECKED,

  /**
   * Provider does not support this verification
   */
  @SerializedName("unsupported")
  UNSUPPORTED,

  /**
   * System error, please retry
   */
  @SerializedName("retry")
  RETRY

}
