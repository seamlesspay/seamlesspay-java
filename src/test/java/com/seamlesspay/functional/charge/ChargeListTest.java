package com.seamlesspay.functional.charge;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.model.Charge;
import com.seamlesspay.model.ChargeCollection;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.param.ChargeCreateParams;
import com.seamlesspay.util.SPLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ChargeListTest {

  private static final SPLogger log = SPLogger.get();

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
    ChargeCreateParams params = ChargeCreateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY + "123")
      .setSeamlessPayAccount("SPAccount")
      .build();

    //when
    AuthenticationException ex = assertThrows(AuthenticationException.class, () -> Charge.list(requestOptions));

    //then
    assertTrue(ex.getMessage().startsWith("Not authenticated error"));
    assertEquals(401, ex.getStatusCode());
  }

  @Test
  void testListChargesSuccessfully() throws SPException {
    //given

    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY)
      .build();

    //when
    ChargeCollection chargeCollection = Charge.list(requestOptions);
    log.info("charge collection = %s", chargeCollection);

    //then
    assertNotNull(chargeCollection.getTotal());
  }
}
