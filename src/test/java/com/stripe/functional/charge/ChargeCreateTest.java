package com.stripe.functional.charge;

import com.seamlesspay.SPAPI;
import com.stripe.exception.ApiException;
import com.stripe.exception.AuthenticationException;
import com.stripe.model.SPCharge;
import com.stripe.net.RequestOptions;
import com.stripe.param.SPChargeCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ChargeCreateTest {

  @InjectMocks
  private SPAPI api;

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
  }

  @Test
  void testAuthenticationExceptionIfApiKeyNotSpecified() {
    //given
    SPChargeCreateParams params = SPChargeCreateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(null)
      .build();

    //when
    AuthenticationException exception = assertThrows(AuthenticationException.class, () -> SPCharge.create(params, requestOptions));

    //then
    assertTrue(exception.getMessage().startsWith("No API key provided"));
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    SPChargeCreateParams params = SPChargeCreateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey("sk_01EWB3GM26X5FE81HQDJ01YK0Y11")
      .build();

    //when
    ApiException ex = assertThrows(ApiException.class, () -> SPCharge.create(params, requestOptions));

    //then
    assertTrue(ex.getMessage().startsWith("Not authenticated error"));
    assertEquals(401, ex.getStatusCode());
  }

  @Test
  void testReturns422IfMissingRequiredFieldToken() {
    //given
    SPChargeCreateParams params = SPChargeCreateParams.builder()
      .amount("1.00")
      .build();

    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey("sk_01EWB3GM26X5FE81HQDJ01YK0Y")
      .build();

    //when
    ApiException ex = assertThrows(ApiException.class, () -> SPCharge.create(params, requestOptions));

    //then
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
    assertEquals(422, ex.getStatusCode());
  }
}
