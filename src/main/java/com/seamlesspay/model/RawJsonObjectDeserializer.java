package com.seamlesspay.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class RawJsonObjectDeserializer implements JsonDeserializer<RawJsonObject> {
  /** Deserializes a JSON payload into a {@link RawJsonObject} object. */
  @Override
  public RawJsonObject deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    RawJsonObject object = new RawJsonObject();
    object.json = json.getAsJsonObject();
    return object;
  }
}
