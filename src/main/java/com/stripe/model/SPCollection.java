package com.stripe.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public abstract class SPCollection<T> extends StripeObject implements SPCollectionInterface<T> {

  @Getter(onMethod_ = {@Override})
  private List<T> data;

  @Getter(onMethod_ = {@Override})
  private SPPagination pagination;

  @Getter(onMethod_ = {@Override})
  private Integer total;

}
