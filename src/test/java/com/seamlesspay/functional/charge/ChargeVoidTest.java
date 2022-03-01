package com.seamlesspay.functional.charge;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.ApiException;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.SPException;
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

  public static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";
  public static final String VALID_TOKEN = "tok_mastercard";

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    ChargeVoidParams params = ChargeVoidParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY + "123")
      .build();

    //when
    Charge charge = new Charge();
    charge.setId("123");
    AuthenticationException ex = assertThrows(AuthenticationException.class, () -> charge.voidCharge(params, requestOptions));

    //then
    assertEquals(401, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Not authenticated error"));
  }

  @Test
  void testReturns422IfTransactionIdIsInvalid() throws SPException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    ChargeCreateParams createParams = ChargeCreateParams.builder().amount("1.00").token(VALID_TOKEN).build();
    Charge charge = Charge.create(createParams, requestOptions);

    //when
    ChargeVoidParams voidParams = ChargeVoidParams.builder().transactionId("123").build();
    ApiException ex = assertThrows(ApiException.class, () -> charge.voidCharge(voidParams, requestOptions));

    //then
    assertEquals(422, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
  }

  @Test
  void testVoidsChargeSuccessfully() throws SPException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    ChargeCreateParams createParams = ChargeCreateParams.builder().amount("1.00").token(VALID_TOKEN).build();
    Charge charge = Charge.create(createParams, requestOptions);
    log.info("created charge=%s", charge);

    //when
    ChargeVoidParams voidParams = ChargeVoidParams.builder().transactionId(charge.getId()).build();
    Charge voidedCharge = charge.voidCharge(voidParams, requestOptions);

    //then
    assertNotNull(voidedCharge);
  }
}
