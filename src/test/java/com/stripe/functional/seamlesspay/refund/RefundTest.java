package com.stripe.functional.seamlesspay.refund;

import com.seamlesspay.SPAPI;
import com.stripe.exception.ApiException;
import com.stripe.exception.StripeException;
import com.stripe.model.SPCharge;
import com.stripe.model.SPRefund;
import com.stripe.model.SPRefundCollection;
import com.stripe.net.RequestOptions;
import com.stripe.param.SPChargeCreateParams;
import com.stripe.param.SPRefundCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static com.stripe.model.SPCurrency.USD;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
class RefundTest {

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
    SPRefundCreateParams params = new SPRefundCreateParams();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY + "123")
      .build();


    //when
    ApiException exception = assertThrows(ApiException.class, () -> SPRefund.create(params, requestOptions));

    //then
    assertEquals(401, exception.getStatusCode());
    assertTrue(exception.getMessage().startsWith("Not authenticated error"));
  }

  @Test
  void testReturns422IfMissingRequiredFieldToken() {
    //given
    SPRefundCreateParams params = SPRefundCreateParams.builder().build();

    //when
    ApiException ex = assertThrows(ApiException.class, () -> SPRefund.create(params, defaultRequestOptions));

    //then
    assertEquals(422, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
  }

  @Test
  void testCreateRefundSuccess() throws StripeException {
    //given
    SPChargeCreateParams params = SPChargeCreateParams.builder()
      .amount("1.00")
      .capture(false)
      .currency(USD)
      .token(VALID_TOKEN)
      .build();
    SPCharge charge = SPCharge.create(params, defaultRequestOptions);

    //when
    SPRefundCreateParams refundParams = SPRefundCreateParams.builder().transactionID(charge.getId()).build();
    SPRefund refund = SPRefund.create(refundParams, defaultRequestOptions);
    log.info("created refund={}", refund);

    //then
    assertNotNull(refund);
  }

  @Test
  void testListRefundSuccess() throws StripeException {
    //given

    //when
    SPRefundCollection list = SPRefund.list(defaultRequestOptions);
    log.info("got refunds list={}", list);

    //then
    assertNotNull(list);
  }

  @Test
  void testRetrieveRefundSuccess() throws StripeException {
    //given
    String existingRefundId = "TR_01E64499PFHYRNYZR8NRH687PG";

    //when
    SPRefund refund = SPRefund.retrieve(existingRefundId, defaultRequestOptions);
    log.info("got refund={}", refund);

    //then
    assertEquals(existingRefundId, refund.getId());
  }


}
