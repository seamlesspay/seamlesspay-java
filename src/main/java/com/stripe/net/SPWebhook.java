package com.stripe.net;

import com.stripe.model.SPEvent;

public final class SPWebhook {

  /**
   * Returns an Event instance using the provided JSON payload. Throws a JsonSyntaxException if the
   * payload is not valid JSON.
   *
   * @param payload the payload sent by Stripe.
   * @return the Event instance
   */
  public static SPEvent constructEvent(String payload) {
    return ApiResource.GSON.fromJson(payload, SPEvent.class);
  }

}
