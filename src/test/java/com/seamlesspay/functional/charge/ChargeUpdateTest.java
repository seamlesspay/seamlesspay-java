package com.seamlesspay.functional.charge;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.ApiException;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.InvalidRequestException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.model.Charge;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.param.ChargeUpdateParams;
import com.seamlesspay.util.SPLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ChargeUpdateTest {

  private static final SPLogger log = SPLogger.get();

  public static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
  }

  @Test
  void testAuthenticationExceptionIfApiKeyNotSpecified() {
    //given
    ChargeUpdateParams params = ChargeUpdateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(null)
      .build();

    //when
    Charge charge = new Charge();
    charge.setId("123");
    AuthenticationException exception = assertThrows(AuthenticationException.class, () -> charge.update(params, requestOptions));

    //then
    assertTrue(exception.getMessage().startsWith("No API key provided"));
  }

  @Test
  void testInvalidRequestExceptionIfNoId() {
    //given
    ChargeUpdateParams params = ChargeUpdateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder().build();

    //when
    Charge charge = new Charge();
    charge.setId(null);
    InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> charge.update(params, requestOptions));

    //then
    assertTrue(exception.getMessage().startsWith("Invalid null ID found"));
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    ChargeUpdateParams params = ChargeUpdateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY + "123")
      .build();

    //when
    Charge charge = new Charge();
    charge.setId("123");
    AuthenticationException ex = assertThrows(AuthenticationException.class, () -> charge.update(params, requestOptions));

    //then
    assertEquals(401, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Not authenticated error"));
  }

  @Test
  void testReturns404OnNotExistingTransactionId() {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    ChargeUpdateParams params = ChargeUpdateParams.builder().amount("1.00").build();

    //when
    Charge charge = new Charge();
    charge.setId("TR_1234567890KMBYB64KRMFPQN6W");
    ApiException ex = assertThrows(ApiException.class, () -> charge.update(params, requestOptions));

    //then
    assertEquals(404, ex.getStatusCode());
  }

  @Test
  void testReturns422IfMissingRequiredField() throws SPException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    Charge existingCharge = Charge.retrieve("TR_01FVFJ0XX7KMBYB64KRMFPQN6W", requestOptions);
    log.info("got existing charge=%s", existingCharge);

    //when
    ChargeUpdateParams params = ChargeUpdateParams.builder().build();
    ApiException ex = assertThrows(ApiException.class, () -> existingCharge.update(params, requestOptions));

    //then
    assertEquals(422, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
  }

  @Test
  void testUpdatesChargeSuccess() throws SPException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    Charge existingCharge = Charge.retrieve("TR_01FVMZ5D1JN1HQ9JKV133B4RYQ", requestOptions);

    double currentAmount = Double.parseDouble(existingCharge.getAmount());
    double newAmount = currentAmount + 1;
    log.info("current amount=%f new=%f", currentAmount, newAmount);

    //when
    String newAmountString = String.format(java.util.Locale.US, "%.2f", newAmount);
    ChargeUpdateParams params = ChargeUpdateParams.builder().amount(newAmountString).build();
    Charge updatedCharge = existingCharge.update(params, requestOptions);

    //then
    assertEquals(newAmountString, updatedCharge.getAmount());
  }
}
