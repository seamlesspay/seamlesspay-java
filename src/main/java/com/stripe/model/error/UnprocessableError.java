package com.stripe.model.error;

import com.google.gson.annotations.SerializedName;
import com.stripe.model.SPObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UnprocessableError extends SPError {

  @SerializedName("data")
  private DataStructure data;

  @Data
  public static class DataStructure {
    @SerializedName("errors")
    private List<String> errors;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusDescription")
    private String statusDescription;
  }

}
