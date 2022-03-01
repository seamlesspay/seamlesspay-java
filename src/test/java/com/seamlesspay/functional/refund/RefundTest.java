package com.seamlesspay.functional.refund;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.ApiException;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.model.*;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.param.ChargeCreateParams;
import com.seamlesspay.param.RefundCreateParams;
import com.seamlesspay.util.SPLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static com.seamlesspay.model.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class RefundTest {

  private static final SPLogger log = SPLogger.get();

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
    RefundCreateParams params = new RefundCreateParams();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY + "123")
      .build();


    //when
    AuthenticationException exception = assertThrows(AuthenticationException.class, () -> Refund.create(params, requestOptions));

    //then
    assertEquals(401, exception.getStatusCode());
    assertTrue(exception.getMessage().startsWith("Not authenticated error"));
  }

  @Test
  void testReturns422IfMissingRequiredFieldToken() {
    //given
    RefundCreateParams params = RefundCreateParams.builder().build();

    //when
    ApiException ex = assertThrows(ApiException.class, () -> Refund.create(params, defaultRequestOptions));

    //then
    assertEquals(422, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
  }

  @Test
  void testCreateRefundSuccess() throws SPException {
    //given
    ChargeCreateParams params = ChargeCreateParams.builder()
      .amount("1.00")
      .capture(true)
      .currency(USD)
      .token(VALID_TOKEN)
      .build();
    Charge charge = Charge.create(params, defaultRequestOptions);
    BatchCloseResult closeBatchResult = Batch.close(charge.getBatch(), defaultRequestOptions);
    assertNotNull(closeBatchResult);

    //when
    RefundCreateParams refundParams = RefundCreateParams.builder().transactionID(charge.getId()).build();
    Refund refund = Refund.create(refundParams, defaultRequestOptions);
    log.debug("created refund=%s", refund);

    //then
    assertNotNull(refund);
  }

  @Test
  void testListRefundSuccess() throws SPException {
    //given

    //when
    RefundCollection list = Refund.list(defaultRequestOptions);
    log.info("got refunds list=%s", list);

    //then
    assertNotNull(list);
  }

  @Test
  void testRetrieveRefundSuccess() throws SPException {
    //given
    String existingRefundId = "TR_01E64499PFHYRNYZR8NRH687PG";

    //when
    Refund refund = Refund.retrieve(existingRefundId, defaultRequestOptions);
    log.debug("got refund=%s", refund);

    //then
    assertEquals(existingRefundId, refund.getId());
  }


}
