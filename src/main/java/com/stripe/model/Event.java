package com.stripe.model;

import com.google.gson.annotations.SerializedName;
import com.stripe.net.ApiResource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Event extends ApiResource implements HasId {


  /** Unique identifier for the object */
  @SerializedName("id")
  private String id;

  /** The webhook event type */
  @SerializedName("event")
  private String event;

  /** The endpoint identifier */
  @SerializedName("endpointID")
  private String endpointID;

  /** The webhook's response */
  @SerializedName("response")
  private Response response;

  /** The endpoint service */
  @SerializedName("service")
  private String service;

  /** The webhook's status */
  @SerializedName("status")
  private String status;

  /** The webhook's sent at attempts */
  @SerializedName("sentAttempts")
  private Integer sentAttempts;

  /** Date of update data */
  @SerializedName("updatedAt")
  private LocalDateTime updatedAt;

  /** Date of create data */
  @SerializedName("createdAt")
  String createdAt;


  @Data
  private static class Response {
    @SerializedName("success")
    private Boolean success;
  }


}
