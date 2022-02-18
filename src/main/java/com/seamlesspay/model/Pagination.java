package com.seamlesspay.model;

import lombok.ToString;

@ToString
public class Pagination {

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
