package com.proptechos.service.filters.actuationiterface;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;
import com.proptechos.model.actuation.RestrictionType;
import java.util.List;

/**
 * RestrictionTypeFilter class for filtering actuation interfaces by restriction types
 *
 * @apiNote Example: 'Range,Enumeration' - Applicable to ActuationInterfaceService
 *
 * (Possible values: [ RegExp | Range | Enumeration | None ])
 *
 * @see com.proptechos.service.ActuationInterfaceService
 */
public class RestrictionTypeFilter implements IQueryFilter {

  private final List<String> restrictionTypes;

  public RestrictionTypeFilter(List<String> restrictionTypes) {
    this.restrictionTypes = restrictionTypes;
  }

  @Override
  public QueryParam queryParam() {
    for (String dataType: restrictionTypes) {
      RestrictionType.valueOf(dataType); // to check for valid DataType
    }
    return new QueryParam("value_restriction_types", String.join(",", restrictionTypes));
  }
}
