package com.stripe.model;

import com.seamlesspay.SPAPI;
import com.stripe.exception.SPException;
import com.stripe.net.ApiResource;
import com.stripe.net.RequestOptions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
