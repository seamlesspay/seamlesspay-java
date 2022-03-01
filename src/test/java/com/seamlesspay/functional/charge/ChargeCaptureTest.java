package com.seamlesspay.functional.charge;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.model.Charge;
import com.seamlesspay.net.RequestOptions;
import com.seamlesspay.param.ChargeCreateParams;
import com.seamlesspay.util.SPLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
class ChargeCaptureTest {

  private static final SPLogger log = SPLogger.get();

  public static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";
  public static final String VALID_TOKEN = "tok_mastercard";

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
  }

  @Test
  void testCaptureChargeSuccessfully() throws SPException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    ChargeCreateParams createParams = ChargeCreateParams.builder()
      .amount("1.00")
      .capture(false)
      .token(VALID_TOKEN).build();
    Charge createdCharge = Charge.create(createParams, requestOptions);
    log.info("created charge=%s", createdCharge);

    //when
    Charge capturedCharge = createdCharge.capture(requestOptions);

    //then
    assertNotNull(capturedCharge);
  }
}
