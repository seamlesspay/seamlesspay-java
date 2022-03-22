package com.seamlesspay.functional.charge;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.functional.Environment;
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

  public static final Environment env = new Environment();

  @InjectMocks
  private SPAPI api;

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(env.getApiBase());
    SPAPI.apiKey = env.getApiKey();
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    ChargeCreateParams params = ChargeCreateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(env.getApiKey() + "123")
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

    //when
    ChargeCollection chargeCollection = Charge.list();
    log.info("charge collection = %s", chargeCollection);

    //then
    assertNotNull(chargeCollection.getTotal());
  }
}
