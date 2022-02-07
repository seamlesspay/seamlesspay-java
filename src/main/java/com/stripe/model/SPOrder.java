package com.stripe.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SPOrder {

  /**
   * Ship From postal code.
   */
  @SerializedName("shipFromPostalCode")
  public final String shipFromPostalCode;

  @SerializedName("shippingAddress")
  public final SPShippingAddress shippingAddress;

  @SerializedName("items")
  public final List<Item> items;

  @Data
  @AllArgsConstructor
  public static class Item {
    /**
     * String with 2 decimal places e.g "25.00".
     */
    @SerializedName("discountAmount")
    String discountAmount;

    /**
     * String with 2 decimal places e.g “25.00”.
     */
    @SerializedName("unitCost")
    String unitCost;

    /**
     * Please be sure to provide a Standard Unit of Measure in order to qualify for Level 3 Interchange pricing.
     */
    @SerializedName("unitOfMeasure")
    String unitOfMeasure;

    /**
     * Line number.
     */
    @SerializedName("lineNumber")
    String lineNumber;

    /**
     * Description.
     */
    @SerializedName("description")
    String description;

    /**
     * String with 2 decimal places e.g “25.00”.
      */
    @SerializedName("taxAmount")
    String taxAmount;

    /**
     * Quantity.
     */
    @SerializedName("quantity")
    String quantity;

    /**
     * Universal product code.
      */
    @SerializedName("upc")
    String upc;

    /**
     * String with 2 decimal places e.g “25.00”.
      */
    @SerializedName("lineTotal")
    String lineTotal;

    @SerializedName("taxExempt")
    boolean taxExempt;

    /**
     * Tax rate expressed as a string with decimal value e.g. for 2.5%, taxRate should be "0.025".
     */
    @SerializedName("taxRate")
    String taxRate;
  }

}
