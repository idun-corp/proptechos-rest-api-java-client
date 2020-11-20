package com.proptechos.service.filters;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;
import java.util.List;

/**
 * BuildingIdsFilter class for filtering by building ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to
 * RealEstateComponentService,
 * BuildingComponentService, RoomService, StoreyService,
 * DeviceService, ActuatorService, SensorService
 *
 * @see com.proptechos.service.RealEstateComponentService
 * @see com.proptechos.service.BuildingComponentService
 * @see com.proptechos.service.RoomService
 * @see com.proptechos.service.StoreyService
 * @see com.proptechos.service.DeviceService
 * @see com.proptechos.service.ActuatorService
 * @see com.proptechos.service.SensorService
 */
public class BuildingIdsFilter implements IQueryFilter {

  private final String buildingIds;

  public BuildingIdsFilter(List<String> buildingIds) {
    this.buildingIds = String.join(",", buildingIds);
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("building_ids", buildingIds);
  }

}
