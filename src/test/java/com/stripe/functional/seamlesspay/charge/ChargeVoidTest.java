package com.stripe.functional.seamlesspay.charge;

import com.seamlesspay.SPAPI;
import com.stripe.exception.ApiException;
import com.stripe.exception.StripeException;
import com.stripe.model.SPCharge;
import com.stripe.net.RequestOptions;
import com.stripe.param.SPChargeCreateParams;
import com.stripe.param.SPChargeVoidParams;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
class ChargeVoidTest {

  public static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";
  public static final String VALID_TOKEN = "tok_mastercard";

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    SPChargeVoidParams params = SPChargeVoidParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY + "123")
      .build();

    //when
    SPCharge spCharge = new SPCharge();
    spCharge.setId("123");
    ApiException ex = assertThrows(ApiException.class, () -> spCharge.voidCharge(params, requestOptions));

    //then
    assertEquals(401, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Not authenticated error"));
  }

  @Test
  void testReturns422IfTransactionIdIsInvalid() throws StripeException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    SPChargeCreateParams createParams = SPChargeCreateParams.builder().amount("1.00").token(VALID_TOKEN).build();
    SPCharge spCharge = SPCharge.create(createParams, requestOptions);

    //when
    SPChargeVoidParams voidParams = SPChargeVoidParams.builder().transactionId("123").build();
    ApiException ex = assertThrows(ApiException.class, () -> spCharge.voidCharge(voidParams, requestOptions));

    //then
    assertEquals(422, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
  }

  @Test
  void testVoidsChargeSuccessfully() throws StripeException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    SPChargeCreateParams createParams = SPChargeCreateParams.builder().amount("1.00").token(VALID_TOKEN).build();
    SPCharge spCharge = SPCharge.create(createParams, requestOptions);
    log.info("created charge={}", spCharge);

    //when
    SPChargeVoidParams voidParams = SPChargeVoidParams.builder().transactionId(spCharge.getId()).build();
    SPCharge voidedCharge = spCharge.voidCharge(voidParams, requestOptions);

    //then
    assertNotNull(voidedCharge);
  }
}
