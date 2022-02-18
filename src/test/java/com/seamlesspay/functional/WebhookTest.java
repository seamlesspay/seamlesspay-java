package com.seamlesspay.functional;

import com.seamlesspay.model.Event;
import com.seamlesspay.net.Webhook;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class WebhookTest {

  @InjectMocks
  private Webhook webhook;

  @Test
  void testConstructsEvent() {
    //given
    String eventJson = "{\"id\":\"WBE_01C56BV08MX9ACFFN84GQEKEGH\",\"service\":\"tickets\"," +
      "\"event\":\"created\",\"endpointID\":\"WBH_01C569BBWRP1KSDDDN098Y3127\",\"status\":\"200\"," +
      "\"sentAttempts\":1,\"response\":{\"success\":true},\"updatedAt\":\"2017-12-15T19:08:18.262Z\"," +
      "\"createdAt\":\"2017-12-15T19:08:18.262Z\"}";

    //when
    Event event = Webhook.constructEvent(eventJson);

    //then
    assertNotNull(event);

  }
}
