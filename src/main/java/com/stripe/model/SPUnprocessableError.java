package com.stripe.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SPUnprocessableError extends StripeObject {

  private Integer code;
  private String className;
  private String message;
  private String name;
  private DataStructure data;

  @Data
  public static class DataStructure {
    private List<String> errors;
    private String statusCode;
    private String statusDescription;
  }

}
