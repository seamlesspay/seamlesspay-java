package com.seamlesspay.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ShippingAddress {

  /**
   * Shipping address.
   */
  @SerializedName("line1")
  String line1;

  /**
   * Shipping second address.
   */
  @SerializedName("line2")
  String line2;

  /**
   * Shipping city.
   */
  @SerializedName("city")
  String city;

  /**
   * Shipping country.
   */
  @SerializedName("country")
  String country;

  /**
   * Shipping state. 2 characters.
   */
  @SerializedName("state")
  String state;

  /**
   * Shipping postal code.
   */
  @SerializedName("postalCode")
  String postalCode;

}
