package com.stripe.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SPNotFoundError extends StripeObject {

  private Integer code;
  private String className;
  private String message;
  private String name;

}
