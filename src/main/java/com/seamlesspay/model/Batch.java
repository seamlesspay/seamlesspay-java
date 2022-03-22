package com.seamlesspay.model;

import com.google.gson.annotations.SerializedName;
import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.net.ApiResource;
import com.seamlesspay.net.RequestOptions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Batch extends ApiResource {

  public static final String BATCHES_URL_PATH = "/batches";
  public static final String CLOSE_BATCHES_URL_PATH = "/close-batches";

  /**
   * The ID of base transaction. Length {@literal <}= 31 characters
   */
  @SerializedName("id")
  private String id;

  @SerializedName("batchStatus")
  private String batchStatus;

  @SerializedName("sales")
  private String sales;

  @SerializedName("refunds")
  private String refunds;

  @SerializedName("autoBatchTime")
  private String autoBatchTime;

  @SerializedName("transaction_ids")
  private Collection<String> transactionIds;

  /**
   * Date of create data
   */
  @SerializedName("openedAt")
  private LocalDateTime openedAt;


  /**
   * Retrieve Batch data by id
   * @return batch found by id or null
   */
  public static Batch retrieve(String batchId) throws SPException {
    return retrieve(batchId, null);
  }

  /**
   * Retrieve Batch data by id
   * @return batch found by id or null
   */
  public static Batch retrieve(String batchId, RequestOptions options) throws SPException {
    String url = String.format("%s%s?_id=%s", SPAPI.getApiBase(), BATCHES_URL_PATH, batchId);
    Map<String, Object> params = new HashMap<>();
    BatchCollection batchCollection = ApiResource.requestSPCollection(url, params, BatchCollection.class, options);
    if (batchCollection.getTotal() != 1) {
      return null;
    }
    return batchCollection.getData().get(0);
  }

  /**
   * Close Batch manually
   */
  public static BatchCloseResult close(String batchId) throws SPException {
    return close(batchId, null);
  }

  /**
   * Close Batch manually
   */
  public static BatchCloseResult close(String batchId, RequestOptions options) throws SPException {
    String url = String.format("%s%s", SPAPI.getApiBase(), CLOSE_BATCHES_URL_PATH);
    Map<String, Object> params = Collections.singletonMap("batchID", batchId);
    return ApiResource.request(RequestMethod.POST, url, params, BatchCloseResult.class, options);
  }

  /**
   * Close Batch manually
   */
  public static ChargeCollection retrieveTransactions(String batchId, RequestOptions options) throws SPException {
    String url = String.format("%s%s/%s", SPAPI.getApiBase(), BATCHES_URL_PATH, batchId);
    Map<String, Object> params = new HashMap<>();
    return ApiResource.requestSPCollection(url, params, ChargeCollection.class, options);
  }

}
