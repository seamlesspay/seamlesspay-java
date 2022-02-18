package com.seamlesspay.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.seamlesspay.net.ApiResource;
import com.seamlesspay.net.SPResponse;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Field;

@EqualsAndHashCode
public abstract class SPObject implements SPObjectInterface {

  public static final Gson PRETTY_PRINT_GSON =
      new GsonBuilder()
          .serializeNulls()
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create();

  @EqualsAndHashCode.Exclude
  private transient SPResponse lastResponse;

  @EqualsAndHashCode.Exclude
  private transient JsonObject rawJsonObject;

  @Override
  public String toString() {
    return String.format(
        "<%s@%s id=%s> JSON: %s",
        this.getClass().getName(),
        System.identityHashCode(this),
        this.getIdString(),
        PRETTY_PRINT_GSON.toJson(this));
  }

  @Override
  public SPResponse getLastResponse() {
    return lastResponse;
  }

  @Override
  public void setLastResponse(SPResponse response) {
    this.lastResponse = response;
  }

  /**
   * Returns the raw JsonObject exposed by the Gson library. This can be used to access properties
   * that are not directly exposed by SeamlessPay's Java library.
   *
   * <p>Note: You should always prefer using the standard property accessors whenever possible.
   * Because this method exposes Gson's underlying API, it is not considered fully stable. SeamlessPay's
   * Java library might move off Gson in the future and this method would be removed or change
   * significantly.
   *
   * @return The raw JsonObject.
   */
  public JsonObject getRawJsonObject() {
    // Lazily initialize this the first time the getter is called.
    if ((this.rawJsonObject == null) && (this.getLastResponse() != null)) {
      this.rawJsonObject =
          ApiResource.GSON.fromJson(this.getLastResponse().body(), JsonObject.class);
    }

    return this.rawJsonObject;
  }

  public String toJson() {
    return PRETTY_PRINT_GSON.toJson(this);
  }

  private Object getIdString() {
    try {
      Field idField = this.getClass().getDeclaredField("id");
      return idField.get(this);
    } catch (SecurityException e) {
      return "";
    } catch (NoSuchFieldException e) {
      return "";
    } catch (IllegalArgumentException e) {
      return "";
    } catch (IllegalAccessException e) {
      return "";
    }
  }

}
