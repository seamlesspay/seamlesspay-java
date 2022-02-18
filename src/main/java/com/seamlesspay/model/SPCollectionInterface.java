package com.seamlesspay.model;

import java.util.List;

public interface SPCollectionInterface<T> extends SPObjectInterface {

  List<T> getData();

  Pagination getPagination();

  Integer getTotal();

}
