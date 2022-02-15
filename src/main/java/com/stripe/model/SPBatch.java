package com.stripe.model;

import com.seamlesspay.SPAPI;
import com.stripe.exception.StripeException;
import com.stripe.net.ApiResource;
import com.stripe.net.RequestOptions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class SPBatch extends ApiResource {

  public static final String BATCHES_URL_PATH = "/batches";
  public static final String CLOSE_BATCHES_URL_PATH = "/close-batches";



  /**
   * Close Batch manually
   */
  public static SPBatchCloseResult close(String batchId) throws StripeException {
    return close(batchId, null);
  }

  /**
   * Close Batch manually
   */
  public static SPBatchCloseResult close(String batchId, RequestOptions options) throws StripeException {
    String url = String.format("%s%s", SPAPI.getApiBase(), CLOSE_BATCHES_URL_PATH);
    Map<String, Object> params = Collections.singletonMap("batchID", batchId);
    return ApiResource.request(RequestMethod.POST, url, params, SPBatchCloseResult.class, options);
  }

}
