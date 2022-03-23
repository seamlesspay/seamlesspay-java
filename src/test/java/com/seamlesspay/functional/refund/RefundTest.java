package com.seamlesspay.functional.refund;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.ApiException;
import com.seamlesspay.exception.AuthenticationException;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.functional.Environment;
import com.seamlesspay.model.*;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.param.ChargeCreateParams;
import com.seamlesspay.param.RefundCreateParams;
import com.seamlesspay.util.SPLogger;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static com.seamlesspay.model.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class RefundTest {

  private static final SPLogger log = SPLogger.get();

  private static final Environment env = new Environment();

  @InjectMocks
  private SPAPI api;

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(env.getApiBase());
    SPAPI.apiKey = env.getApiKey();
  }

  @Test
  void testGet401IfInvalidApiKey() {
    //given
    RefundCreateParams params = new RefundCreateParams();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(env.getApiKey() + "123")
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
    ApiException ex = assertThrows(ApiException.class, () -> Refund.create(params));

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
      .token(env.getValidToken())
      .build();
    Charge charge = Charge.create(params);
    BatchCloseResult closeBatchResult = Batch.close(charge.getBatch());
    assertNotNull(closeBatchResult);

    Awaitility.await()
      .pollInterval(500, TimeUnit.MILLISECONDS)
      .atMost(10_000, TimeUnit.MILLISECONDS)
      .until(() -> Charge.retrieve(charge.getId()).getStatus().equals(TransactionStatus.SETTLED));

    //when
    RefundCreateParams refundParams = RefundCreateParams.builder().transactionID(charge.getId()).build();
    Refund refund = Refund.create(refundParams);
    log.debug("created refund=%s", refund);

    //then
    assertNotNull(refund);
    Charge.retrieve(charge.getId());
  }

  @Test
  void testListRefundSuccess() throws SPException {
    //given

    //when
    RefundCollection list = Refund.list();
    log.info("got refunds list=%s", list);

    //then
    assertNotNull(list);
  }

  @Test
  void testRetrieveRefundSuccess() throws SPException {
    //given
    String existingRefundId = "TR_01FYA5ZFVTWYZ6VNPAG63ZCDVY";

    //when
    Refund refund = Refund.retrieve(existingRefundId);
    log.debug("got refund=%s", refund);

    //then
    assertEquals(existingRefundId, refund.getId());
  }


}
