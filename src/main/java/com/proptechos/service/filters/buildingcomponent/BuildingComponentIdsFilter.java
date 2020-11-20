package com.proptechos.service.filters.buildingcomponent;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;
import java.util.List;

/**
 * BuildingComponentIdsFilter class for filtering by building component ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to
 * BuildingComponentService,
 * DeviceService, ActuatorService, SensorService
 *
 * @see com.proptechos.service.BuildingComponentService
 * @see com.proptechos.service.DeviceService
 * @see com.proptechos.service.ActuatorService
 * @see com.proptechos.service.SensorService
 */
public class BuildingComponentIdsFilter implements IQueryFilter {

  private final String buildingComponentIds;

  public BuildingComponentIdsFilter(List<String> buildingComponentIds) {
    this.buildingComponentIds = String.join(",", buildingComponentIds);
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("buildingcomponent_ids", buildingComponentIds);
  }

}
