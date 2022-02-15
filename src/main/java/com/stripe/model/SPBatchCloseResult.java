package com.stripe.model;

import com.google.gson.annotations.SerializedName;
import com.stripe.net.ApiResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SPBatchCloseResult extends ApiResource {

  /**
   * Transaction data
   */
  @SerializedName("data")
  private TransactionData data;

  /**
   * Result message
   */
  @SerializedName("message")
  private String message;

  @SerializedName("input")
  private Input input;


  @Data
  private static class TransactionData {

    /**
     * Count of affected transactions.
     */
    @SerializedName("transactions")
    private Integer transactions;

    /**
     * Array of batches unique identifiers.
     */
    @SerializedName("batches")
    private List<String> batches;
  }

  @Data
  private static class Input {

    /**
     * The batch's unique identifier
     */
    @SerializedName("batchId")
    private String batchId;

    @SerializedName("env")
    private String env;

    /**
     * The merchant's unique identifier
     */
    @SerializedName("merchantId")
    private String merchantId;

  }

}
