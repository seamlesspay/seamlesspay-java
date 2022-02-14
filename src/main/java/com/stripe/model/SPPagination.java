package com.stripe.model;

import lombok.ToString;

@ToString
public class SPPagination {

  /**
   * Results returned per page
   */
  private Integer count;

  /**
   * Page number
   */
  private Integer page;

  /**
   * Total number of pages
   */
  private Integer pages;

  /**
   * Total results from query
   */
  private Integer size;

}
