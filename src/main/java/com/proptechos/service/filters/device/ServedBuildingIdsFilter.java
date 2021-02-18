package com.proptechos.service.filters.device;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;
import java.util.List;

/**
 * ServedBuildingIdsFilter class for filtering by building ids that are served by devices
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to DeviceService, SensorService, ActuatorService
 *
 * @see com.proptechos.service.DeviceService
 * @see com.proptechos.service.SensorService
 * @see com.proptechos.service.ActuatorService
 */
public class ServedBuildingIdsFilter implements IQueryFilter {

  private final String servedBuildingIds;

  public ServedBuildingIdsFilter(List<String> servedBuildingIds) {
    this.servedBuildingIds = String.join(",", servedBuildingIds);
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("served_building_ids", servedBuildingIds);
  }

}
