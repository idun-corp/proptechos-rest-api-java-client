package com.proptechos.service.filters.location;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

/**
 * DistanceFilter class used for filtering real estates and building by range
 *
 * @apiNote Example: '10.0' - Applicable only to RealEstateService, RealEstateComponentService
 *
 * @see com.proptechos.service.RealEstateService
 * @see com.proptechos.service.RealEstateComponentService
 */
public class DistanceFilter implements IQueryFilter {

  private final double distance;

  public DistanceFilter(double distance) {
    this.distance = distance;
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("dist", String.valueOf(distance));
  }

}
