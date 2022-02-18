package com.stripe.model;

import com.stripe.net.SPResponse;

public interface SPObjectInterface {

  /**
   * Keeps last response from SeamlessPay. Useful for debugging and getting some extra-info
   */
  SPResponse getLastResponse();
  void setLastResponse(SPResponse response);

}
