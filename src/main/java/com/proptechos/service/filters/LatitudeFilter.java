package com.proptechos.service.filters;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

/**
 * LatitudeFilter class used for filtering real estates and building by range
 *
 * @apiNote Example: '59' - Applicable only to RealEstateService, RealEstateComponentService
 *
 * @see com.proptechos.service.RealEstateService
 * @see com.proptechos.service.RealEstateComponentService
 */
public class LatitudeFilter implements IQueryFilter {

  private final double latitude;

  public LatitudeFilter(double latitude) {
    this.latitude = latitude;
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("lat", String.valueOf(latitude));
  }

}
