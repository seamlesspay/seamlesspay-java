package com.seamlesspay.model;

import com.seamlesspay.net.SPResponse;

public interface SPObjectInterface {

  /**
   * Keeps last response from SeamlessPay. Useful for debugging and getting some extra-info
   */
  SPResponse getLastResponse();
  void setLastResponse(SPResponse response);

}
