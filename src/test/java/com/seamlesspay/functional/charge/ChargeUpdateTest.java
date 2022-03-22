package com.seamlesspay.functional.charge;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.ApiException;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.InvalidRequestException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.functional.Environment;
import com.seamlesspay.model.Charge;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.param.ChargeCreateParams;
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

  public static final Environment env = new Environment();

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(env.getApiBase());
    SPAPI.apiKey = env.getApiKey();
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
      .setApiKey(env.getApiKey() + "123")
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
    ChargeUpdateParams params = ChargeUpdateParams.builder().amount("1.00").build();

    //when
    Charge charge = new Charge();
    charge.setId("TR_1234567890KMBYB64KRMFPQN6W");
    ApiException ex = assertThrows(ApiException.class, () -> charge.update(params));

    //then
    assertEquals(404, ex.getStatusCode());
  }

  @Test
  void testReturns422IfMissingRequiredField() throws SPException {
    //given
    Charge existingCharge = Charge.retrieve("TR_01FY7K3QP1QMGNK48J1ZCDSPFF");
    log.info("got existing charge=%s", existingCharge);

    //when
    ChargeUpdateParams params = ChargeUpdateParams.builder().build();
    ApiException ex = assertThrows(ApiException.class, () -> existingCharge.update(params));

    //then
    assertEquals(422, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
  }

  @Test
  void testUpdatesChargeSuccess() throws SPException {
    //given
    ChargeCreateParams createParams = ChargeCreateParams.builder()
      .amount("1.00")
      .capture(false)
      .token(env.getValidToken())
      .build();
    Charge createdCharge = Charge.create(createParams);
    log.info("created charge=%s", createdCharge);

    double currentAmount = Double.parseDouble("1.00");
    double newAmount = currentAmount + 1;
    log.info("current amount=%f new=%f", currentAmount, newAmount);

    //when
    String newAmountString = String.format(java.util.Locale.US, "%.2f", newAmount);
    ChargeUpdateParams params = ChargeUpdateParams.builder().amount(newAmountString).build();
    Charge updatedCharge = createdCharge.update(params);

    //then
    assertEquals(newAmountString, updatedCharge.getAmount());
  }
}
