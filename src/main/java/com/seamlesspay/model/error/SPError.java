package com.seamlesspay.model.error;

import com.google.gson.annotations.SerializedName;
import com.seamlesspay.model.SPObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SPError extends SPObject {

  @SerializedName("code")
  private Integer code;

  @SerializedName("className")
  private String className;

  @SerializedName("message")
  private String message;

  @SerializedName("name")
  private String name;

}
