package com.seamlesspay.net;

import com.seamlesspay.exception.SPException;
import com.seamlesspay.model.SPObjectInterface;

import java.io.InputStream;
import java.util.Map;

public interface ResponseGetter {
  <T extends SPObjectInterface> T request(
      ApiResource.RequestMethod method,
      String url,
      Map<String, Object> params,
      Class<T> clazz,
      RequestOptions options)
      throws SPException;

  default InputStream requestStream(
      ApiResource.RequestMethod method,
      String url,
      Map<String, Object> params,
      RequestOptions options)
      throws SPException {
    throw new UnsupportedOperationException(
        "requestStream is unimplemented for this ResponseGetter");
  }
}
