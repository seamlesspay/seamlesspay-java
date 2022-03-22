package com.seamlesspay.functional.charge;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.ApiException;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.functional.Environment;
import com.seamlesspay.model.Charge;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.param.ChargeCreateParams;
import com.seamlesspay.param.ChargeVoidParams;
import com.seamlesspay.util.SPLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ChargeVoidTest {

  private static final SPLogger log = SPLogger.get();

  public static final Environment env = new Environment();

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(env.getApiBase());
    SPAPI.apiKey = env.getApiKey();
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    ChargeVoidParams params = ChargeVoidParams.builder().transactionId("123").build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(env.getApiKey() + "123")
      .build();

    //when
    AuthenticationException ex = assertThrows(AuthenticationException.class, () -> Charge.voidCharge(params, requestOptions));

    //then
    assertEquals(401, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Not authenticated error"));
  }

  @Test
  void testReturns404IfTransactionIdIsInvalid() throws SPException {
    //given

    //when
    ChargeVoidParams voidParams = ChargeVoidParams.builder().transactionId("123").build();
    ApiException ex = assertThrows(ApiException.class, () -> Charge.voidCharge(voidParams));

    //then
    assertEquals(404, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Not found"));
  }

  @Test
  void testVoidsChargeSuccessfully() throws SPException {
    //given
    ChargeCreateParams createParams = ChargeCreateParams.builder().amount("1.00").token(env.getValidToken()).build();
    Charge charge = Charge.create(createParams);
    log.info("created charge=%s", charge);

    //when
    ChargeVoidParams voidParams = ChargeVoidParams.builder().transactionId(charge.getId()).build();
    Charge voidedCharge = Charge.voidCharge(voidParams);

    //then
    assertNotNull(voidedCharge);
  }
}
