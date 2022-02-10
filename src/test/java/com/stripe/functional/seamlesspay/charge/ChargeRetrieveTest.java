package com.stripe.functional.seamlesspay.charge;

import com.seamlesspay.SPAPI;
import com.stripe.exception.ApiException;
import com.stripe.exception.StripeException;
import com.stripe.model.SPCharge;
import com.stripe.net.RequestOptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
class ChargeRetrieveTest {

  public static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";
  public static final String EXISTING_TRANSACTION_ID = "TR_01E5DR09Z6FCS1TBYMNVQC4QXC";

  @InjectMocks
  private SPAPI api;

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY + "123")
      .build();

    //when
    ApiException ex = assertThrows(ApiException.class, () -> SPCharge.retrieve("", requestOptions));

    //then
    assertTrue(ex.getMessage().startsWith("Not authenticated error"));
    assertEquals(401, ex.getStatusCode());
  }

  @Test
  void testReturns404IfInvalidTransactionId() {
    //given
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY)
      .build();

    //when
    ApiException ex = assertThrows(ApiException.class, () -> SPCharge.retrieve("not_existing_transaction_id", requestOptions));
    log.info("error", ex);

    //then
    assertTrue(ex.getMessage().startsWith("Not found error"));
    assertEquals(404, ex.getStatusCode());
  }

  @Test
  void testRetrieveChargeSuccessfully() throws StripeException {
    //given
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY)
      .build();

    //when
    SPCharge charge = SPCharge.retrieve(EXISTING_TRANSACTION_ID, requestOptions);
    log.info("got charge={}", charge);

    //then
    assertNotNull(charge);
  }
}
