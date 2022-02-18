package com.seamlesspay.model.error;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class NotAuthenticatedError extends SPError {

  @SerializedName("errors")
  private List<String> errors;

}
