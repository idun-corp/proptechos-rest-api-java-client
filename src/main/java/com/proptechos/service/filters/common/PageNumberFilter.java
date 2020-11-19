package com.proptechos.service.filters.common;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

/**
 * PageNumberFilter class for filtering by page number
 *
 * @apiNote Example: '10' - Applicable only to PagedService
 *
 */
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
