package com.stripe.functional.seamlesspay.charge;

import com.seamlesspay.SPAPI;
import com.stripe.exception.ApiException;
import com.stripe.exception.StripeException;
import com.stripe.model.SPCharge;
import com.stripe.model.SPChargeCollection;
import com.stripe.net.RequestOptions;
import com.stripe.param.SPChargeCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ChargeListTest {

  public static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";
  public static final String VALID_TOKEN = "TKN_01EXMB975XXG1XA3MATBNBR4QF";

  @InjectMocks
  private SPAPI api;

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    SPChargeCreateParams params = SPChargeCreateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey("sk_01EWB3GM26X5FE81HQDJ01YK0Y11")
      .setSeamlessPayAccount("SPAccount")
      .build();

    //when
    ApiException ex = assertThrows(ApiException.class, () -> SPCharge.list(requestOptions));

    //then
    assertTrue(ex.getMessage().startsWith("Not authenticated error"));
    assertEquals(401, ex.getStatusCode());
  }

  @Test
  void testListChargesSuccessfully() throws StripeException {
    //given

    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY)
      .build();

    //when
    SPChargeCollection chargeCollection = SPCharge.list(requestOptions);

    //then
    assertNotNull(chargeCollection.getTotal());
  }
}
