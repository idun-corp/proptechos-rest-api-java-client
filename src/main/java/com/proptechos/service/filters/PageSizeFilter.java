package com.proptechos.service.filters;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

public class PageSizeFilter implements IQueryFilter {

  private long pageSize;

  public PageSizeFilter(long pageSize) {
    this.pageSize = pageSize;
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("size", String.valueOf(pageSize));
  }
}
