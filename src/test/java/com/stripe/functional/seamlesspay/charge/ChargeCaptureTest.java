package com.stripe.functional.seamlesspay.charge;

import com.seamlesspay.SPAPI;
import com.stripe.exception.StripeException;
import com.stripe.model.SPCharge;
import com.stripe.net.RequestOptions;
import com.stripe.param.SPChargeCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
class ChargeCaptureTest {

  public static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";
  public static final String VALID_TOKEN = "tok_mastercard";

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
  }

  @Test
  void testCaptureChargeSuccessfully() throws StripeException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    SPChargeCreateParams createParams = SPChargeCreateParams.builder()
      .amount("1.00")
      .capture(false)
      .token(VALID_TOKEN).build();
    SPCharge createdCharge = SPCharge.create(createParams, requestOptions);
    log.info("created charge={}", createdCharge);

    //when
    SPCharge capturedCharge = createdCharge.capture(requestOptions);

    //then
    assertNotNull(capturedCharge);
  }
}
