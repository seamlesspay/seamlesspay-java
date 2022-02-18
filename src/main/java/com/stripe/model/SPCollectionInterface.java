package com.stripe.model;

import java.util.List;

public interface SPCollectionInterface<T> extends SPObjectInterface {

  List<T> getData();

  Pagination getPagination();

  Integer getTotal();

}
