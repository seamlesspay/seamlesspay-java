package com.seamlesspay.functional.batch;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.ApiException;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.functional.Environment;
import com.seamlesspay.model.Batch;
import com.seamlesspay.model.BatchCloseResult;
import com.seamlesspay.model.Charge;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.param.ChargeCreateParams;
import com.seamlesspay.util.SPLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.seamlesspay.model.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class BatchCloseTest {

  private static final SPLogger log = SPLogger.get();

  private static final Environment env = new Environment();


  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(env.getApiBase());
    SPAPI.apiKey = env.getApiKey();
  }

  @Test
  void testGet401IfInvalidApiKey() {
    //given
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(env.getApiKey() + "123")
      .build();


    //when
    AuthenticationException exception = assertThrows(AuthenticationException.class, () -> Batch.close(null, requestOptions));

    //then
    assertEquals(401, exception.getStatusCode());
    assertTrue(exception.getMessage().startsWith("Not authenticated error"));
  }

  @Test
  void testReturns422IfMissingRequiredFieldToken() {
    //given

    //when
    ApiException ex = assertThrows(ApiException.class, () -> Batch.close(null));

    //then
    assertEquals(422, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
  }

  @Test
  void testBatchCloseSuccess() throws SPException {
    //given
    ChargeCreateParams params = ChargeCreateParams.builder()
      .amount("1.00")
      .capture(true)
      .currency(USD)
      .token(env.getValidToken())
      .build();
    Charge charge = Charge.create(params);

    //when
    BatchCloseResult closeResult = Batch.close(charge.getBatch());
    log.info("batch close result=%s", closeResult);

  }

}
