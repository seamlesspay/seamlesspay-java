package com.stripe.functional.seamlesspay.charge;

import com.seamlesspay.SPAPI;
import com.stripe.exception.ApiException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.StripeException;
import com.stripe.model.SPCharge;
import com.stripe.model.SPOrder;
import com.stripe.model.SPShippingAddress;
import com.stripe.net.RequestOptions;
import com.stripe.param.SPChargeCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.stripe.model.SPCurrency.USD;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ChargeCreateTest {

  public static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";
  public static final String VALID_TOKEN = "TKN_01EXMB975XXG1XA3MATBNBR4QF";

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
      .setApiKey(DEV_API_KEY)
      .build();

    //when
    ApiException ex = assertThrows(ApiException.class, () -> SPCharge.create(params, requestOptions));

    //then
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
    assertEquals(422, ex.getStatusCode());
  }

  @Test
  void testCreatesChargeIfSuccess() throws StripeException {
    //given
    List<SPOrder.Item> orderItems = Arrays.asList(
      new SPOrder.Item(
        "20.00",
        "10.00",
        "CS",
        "1",
        "DESCRIPTION-1",
        "5.00",
        "12",
        "UPC-1",
        "105.00",
        false,
        "0.05"
      )
    );

    SPOrder spOrder = new SPOrder("99999-9999",
      new SPShippingAddress("400 Madison Ave",
        "10th Fl",
        "New York",
        "NY",
        "US",
        "11111-1111"),
      orderItems
    );

    SPChargeCreateParams params = SPChargeCreateParams.builder()
      .amount("1.00")
      .capture(false)
      .currency(USD)
      .cvv("123")
      .order(spOrder)
      .token(VALID_TOKEN)
      .build();

    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY)
      .build();

    //when
    SPCharge charge = SPCharge.create(params, requestOptions);

    //then
    assertEquals("1.00", charge.getAmount());
  }
}
