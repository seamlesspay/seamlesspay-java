package com.stripe.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SPNotAuthenticatedError extends StripeObject {

  private Integer code;
  private String className;
  private String message;
  private String name;
  private List<String> errors;

}
