package com.seamlesspay.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public abstract class SPCollection<T> extends SPObject implements SPCollectionInterface<T> {

  @Getter(onMethod_ = {@Override})
  private List<T> data;

  @Getter(onMethod_ = {@Override})
  private Pagination pagination;

  @Getter(onMethod_ = {@Override})
  private Integer total;

}
