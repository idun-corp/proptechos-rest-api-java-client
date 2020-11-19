package com.proptechos.service.filters.location;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

/**
 * LongitudeFilter class used for filtering real estates and building by range
 *
 * @apiNote Example: '18' - Applicable only to RealEstateService, RealEstateComponentService
 *
 * @see com.proptechos.service.RealEstateService
 * @see com.proptechos.service.RealEstateComponentService
 */
public class LongitudeFilter implements IQueryFilter {

  private final double longitude;

  public LongitudeFilter(double longitude) {
    this.longitude = longitude;
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("lon", String.valueOf(longitude));
  }
}
