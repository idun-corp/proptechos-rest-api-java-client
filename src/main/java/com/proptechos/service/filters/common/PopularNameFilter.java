package com.proptechos.service.filters.common;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

/**
 * PopularNameFilter class for filtering by full popular name or by popular name part
 *
 * @apiNote Example: 'TestName','Test'
 *
 */
public class PopularNameFilter implements IQueryFilter {

  private final String popularName;

  public PopularNameFilter(String popularName) {
    this.popularName = popularName;
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("popular_name", popularName);
  }
}
