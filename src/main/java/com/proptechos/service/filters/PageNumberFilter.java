package com.proptechos.service.filters;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

public class PageNumberFilter implements IQueryFilter {

  private final long pageNumber;

  public PageNumberFilter(long pageNumber) {
    this.pageNumber = pageNumber;
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("page", String.valueOf(pageNumber));
  }
}
