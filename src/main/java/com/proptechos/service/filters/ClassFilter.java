package com.proptechos.service.filters;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

/**
 * ClassFilter class for filtering by real estate core class
 *
 * @apiNote Example: 'Sensor' - Applicable only to DeviceService
 *
 * @see com.proptechos.service.DeviceService
 */
public class ClassFilter implements IQueryFilter {

  private final String recClass;

  public ClassFilter(String recClass) {
    this.recClass = recClass;
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("class", recClass);
  }
}
