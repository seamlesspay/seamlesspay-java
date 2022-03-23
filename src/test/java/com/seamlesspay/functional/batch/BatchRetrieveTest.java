package com.seamlesspay.functional.batch;

import com.seamlesspay.SPAPI;
import com.seamlesspay.exception.SPException;
import com.seamlesspay.functional.Environment;
import com.seamlesspay.model.Batch;
import com.seamlesspay.model.Charge;
import com.seamlesspay.param.ChargeCreateParams;
import com.seamlesspay.util.SPLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.seamlesspay.model.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class BatchRetrieveTest {

  private static final SPLogger log = SPLogger.get();
  private static final Environment env = new Environment();

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(env.getApiBase());
    SPAPI.apiKey = env.getApiKey();
  }

  @Test
  void testBatchRetrieveById() throws SPException {
    //given
    ChargeCreateParams params = ChargeCreateParams.builder()
      .amount("1.00")
      .capture(true)
      .currency(USD)
      .token(env.getValidToken())
      .build();
    Charge charge = Charge.create(params);


    //when
    String batchId = charge.getBatch();
    Batch batch = Batch.retrieve(batchId);

    //then
    assertEquals(batchId, batch.getId());
  }

}
