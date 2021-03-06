package com.seamlesspay.param.common;

import com.google.gson.annotations.SerializedName;
import com.seamlesspay.net.ApiRequestParams;

public enum EmptyParam implements ApiRequestParams.EnumParam {
  @SerializedName("")
  EMPTY("");
  private final String value;

  EmptyParam(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return this.value;
  }
}
