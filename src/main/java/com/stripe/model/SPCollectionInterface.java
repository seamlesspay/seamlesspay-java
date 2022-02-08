package com.stripe.model;

import java.util.List;

public interface SPCollectionInterface<T> extends StripeObjectInterface {

  List<T> getData();

  SPPagination getPagination();

  Integer getTotal();

}
