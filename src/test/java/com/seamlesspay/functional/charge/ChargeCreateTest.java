package com.seamlesspay.functional.charge;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.ApiException;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.model.Charge;
import com.seamlesspay.model.Order;
import com.seamlesspay.model.ShippingAddress;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.param.ChargeCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.seamlesspay.model.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ChargeCreateTest {

  public static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";
  public static final String VALID_TOKEN = "tok_mastercard";

  @InjectMocks
  private SPAPI api;

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
    SPAPI.apiKey = DEV_API_KEY;
  }

  @Test
  void testAuthenticationExceptionIfApiKeyNotSpecified() {
    //given
    ChargeCreateParams params = ChargeCreateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(null)
      .build();

    //when
    AuthenticationException exception = assertThrows(AuthenticationException.class, () -> Charge.create(params, requestOptions));

    //then
    assertTrue(exception.getMessage().startsWith("No API key provided"));
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    ChargeCreateParams params = ChargeCreateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY + "123")
      .build();

    //when
    AuthenticationException ex = assertThrows(AuthenticationException.class, () -> Charge.create(params, requestOptions));

    //then
    assertTrue(ex.getMessage().startsWith("Not authenticated error"));
    assertEquals(401, ex.getStatusCode());
  }

  @Test
  void testReturns422IfMissingRequiredFieldToken() {
    //given
    ChargeCreateParams params = ChargeCreateParams.builder()
      .amount("1.00")
      .build();

    //when
    ApiException ex = assertThrows(ApiException.class, () -> Charge.create(params));

    //then
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
    assertEquals(422, ex.getStatusCode());
  }

  @Test
  void testCreatesChargeIfSuccess() throws SPException {
    //given
    List<Order.Item> orderItems = Arrays.asList(
      new Order.Item(
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

    Order order = new Order("99999-9999",
      new ShippingAddress("400 Madison Ave",
        "10th Fl",
        "New York",
        "NY",
        "US",
        "11111-1111"),
      orderItems
    );

    ChargeCreateParams params = ChargeCreateParams.builder()
      .amount("1.00")
      .capture(true)
      .currency(USD)
      .cvv("123")
      .order(order)
      .token(VALID_TOKEN)
      .build();

    //when
    Charge charge = Charge.create(params);

    //then
    assertEquals("1.00", charge.getAmount());
  }
}
