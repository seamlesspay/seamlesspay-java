package com.stripe.functional.seamlesspay.charge;

import com.seamlesspay.SPAPI;
import com.stripe.exception.ApiException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.SPCharge;
import com.stripe.net.RequestOptions;
import com.stripe.param.SPChargeUpdateParams;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
class ChargeUpdateTest {

  public static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
  }

  @Test
  void testAuthenticationExceptionIfApiKeyNotSpecified() {
    //given
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(null)
      .build();

    //when
    SPCharge spCharge = new SPCharge();
    spCharge.setId("123");
    AuthenticationException exception = assertThrows(AuthenticationException.class, () -> spCharge.update(params, requestOptions));

    //then
    assertTrue(exception.getMessage().startsWith("No API key provided"));
  }

  @Test
  void testInvalidRequestExceptionIfNoId() {
    //given
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder().build();

    //when
    SPCharge spCharge = new SPCharge();
    spCharge.setId(null);
    InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> spCharge.update(params, requestOptions));

    //then
    assertTrue(exception.getMessage().startsWith("Invalid null ID found"));
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY + "123")
      .build();

    //when
    SPCharge spCharge = new SPCharge();
    spCharge.setId("123");
    ApiException ex = assertThrows(ApiException.class, () -> spCharge.update(params, requestOptions));

    //then
    assertEquals(401, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Not authenticated error"));
  }

  @Test
  void testReturns404OnNotExistingTransactionId() {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().amount("1.00").build();

    //when
    SPCharge spCharge = new SPCharge();
    spCharge.setId("TR_1234567890KMBYB64KRMFPQN6W");
    ApiException ex = assertThrows(ApiException.class, () -> spCharge.update(params, requestOptions));

    //then
    assertEquals(404, ex.getStatusCode());
  }

  @Test
  void testReturns422IfMissingRequiredField() throws StripeException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    SPCharge existingCharge = SPCharge.retrieve("TR_01FVFJ0XX7KMBYB64KRMFPQN6W", requestOptions);
    log.info("got existing charge={}", existingCharge);

    //when
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().amount("123.00").build();
    ApiException ex = assertThrows(ApiException.class, () -> existingCharge.update(params, requestOptions));

    //then
    assertEquals(422, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
  }

  @Test
  void testUpdatesChargeIfSuccess() throws StripeException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    SPCharge existingCharge = SPCharge.retrieve("TR_01FVJXHT64V8X632R0HCMJXMEM", requestOptions);

    double currentAmount = Double.parseDouble(existingCharge.getAmount());
    double newAmount = currentAmount + 1;
    log.info("current amount={} new={}", currentAmount, newAmount);

    //when
    String newAmountString = String.format(java.util.Locale.US, "%.2f", newAmount);
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().amount(newAmountString).build();
    SPCharge updatedCharge = existingCharge.update(params, requestOptions);

    //then
    assertEquals(newAmountString, updatedCharge.getAmount());
  }
}
