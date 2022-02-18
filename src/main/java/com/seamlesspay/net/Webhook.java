package com.seamlesspay.net;

import com.seamlesspay.model.Event;

public final class Webhook {

  /**
   * Returns an Event instance using the provided JSON payload. Throws a JsonSyntaxException if the
   * payload is not valid JSON.
   *
   * @param payload the payload sent by SeamlessPay.
   * @return the Event instance
   */
  public static Event constructEvent(String payload) {
    return ApiResource.GSON.fromJson(payload, Event.class);
  }

}
