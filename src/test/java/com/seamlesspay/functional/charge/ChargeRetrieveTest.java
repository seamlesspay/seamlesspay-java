package com.seamlesspay.functional.charge;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.ApiException;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.model.Charge;
import com.seamlesspay.net.RequestOptions;
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
    AuthenticationException ex = assertThrows(AuthenticationException.class, () -> Charge.retrieve("", requestOptions));

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
    ApiException ex = assertThrows(ApiException.class, () -> Charge.retrieve("not_existing_transaction_id", requestOptions));
    log.info("error", ex);

    //then
    assertTrue(ex.getMessage().startsWith("Not found error"));
    assertEquals(404, ex.getStatusCode());
  }

  @Test
  void testRetrieveChargeSuccessfully() throws SPException {
    //given
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY)
      .build();

    //when
    Charge charge = Charge.retrieve(EXISTING_TRANSACTION_ID, requestOptions);
    log.info("got charge={}", charge);

    //then
    assertNotNull(charge);
  }
}
