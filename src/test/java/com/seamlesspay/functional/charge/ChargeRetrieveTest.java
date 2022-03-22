package com.seamlesspay.functional.charge;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.ApiException;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.functional.Environment;
import com.seamlesspay.model.Charge;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.util.SPLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ChargeRetrieveTest {

  private static final SPLogger log = SPLogger.get();
  public static final Environment env = new Environment();

  public static final String EXISTING_TRANSACTION_ID = "TR_01FYF7ZFM3MXECVJ4BKWK0HQY1";

  @InjectMocks
  private SPAPI api;

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(env.getApiBase());
    SPAPI.apiKey = env.getApiKey();
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(env.getApiKey() + "123")
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

    //when
    ApiException ex = assertThrows(ApiException.class, () -> Charge.retrieve("not_existing_transaction_id"));
    log.info("error", ex);

    //then
    assertTrue(ex.getMessage().startsWith("Not found error"));
    assertEquals(404, ex.getStatusCode());
  }

  @Test
  void testRetrieveChargeSuccessfully() throws SPException {
    //given

    //when
    Charge charge = Charge.retrieve(EXISTING_TRANSACTION_ID);
    log.info("got charge={}", charge);

    //then
    assertNotNull(charge);
  }
}
