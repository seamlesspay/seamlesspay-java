package com.seamlesspay.functional;

import com.seamlesspay.SPAPI;
import lombok.Data;

@Data
public class Environment {

  private String apiBase = SPAPI.SANDBOX_API_BASE;
  private String apiKey = "sk_01FXDMBVBMP2AYF7NQH8WF77C8";
  private String validToken = "TKN_01FY9MKE7XKP9MS4JJ000WX0Z4";


}
