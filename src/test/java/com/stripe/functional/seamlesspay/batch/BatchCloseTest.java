package com.stripe.functional.seamlesspay.batch;

import com.seamlesspay.SPAPI;
import com.stripe.exception.ApiException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.SPException;
import com.stripe.model.Batch;
import com.stripe.model.BatchCloseResult;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import com.stripe.param.ChargeCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static com.stripe.model.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
class BatchCloseTest {

  private static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";
  private static final String VALID_TOKEN = "tok_mastercard";

  @InjectMocks
  private SPAPI api;
  private RequestOptions defaultRequestOptions;

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
    defaultRequestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
  }

  @Test
  void testGet401IfInvalidApiKey() {
    //given
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY + "123")
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
    ApiException ex = assertThrows(ApiException.class, () -> Batch.close(null, defaultRequestOptions));

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
//      .cvv("123")
      .token(VALID_TOKEN)
      .build();
    Charge charge = Charge.create(params, defaultRequestOptions);

    //when
    BatchCloseResult closeResult = Batch.close(charge.getBatch(), defaultRequestOptions);
//    SPRefundCreateParams refundParams = SPRefundCreateParams.builder().transactionID(charge.getId()).build();
//    SPBatch batch = SPBatch.close(refundParams, defaultRequestOptions);
    log.info("batch close result={}", closeResult);

    //then
//    assertNotNull(refund);
  }

}
