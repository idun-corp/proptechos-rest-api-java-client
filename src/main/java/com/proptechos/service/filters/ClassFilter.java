package com.proptechos.service.filters;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

public class ClassFilter implements IQueryFilter {

  private String recClass;

  public ClassFilter(String recClass) {
    this.recClass = recClass;
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("class", recClass);
  }
}
