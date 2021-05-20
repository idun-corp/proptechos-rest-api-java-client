package com.proptechos.service.filters.device;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * PlacementContextFilter class for filtering by placement context names
 *
 * @apiNote Example: 'IndoorAir,OutdoorAir' - Applicable to DeviceService, ActuatorService, SensorService
 * @see com.proptechos.service.DeviceService
 * @see com.proptechos.service.ActuatorService
 * @see com.proptechos.service.SensorService
 */
public class PlacementContextFilter implements IQueryFilter {

    private final String placementContexts;

    public PlacementContextFilter(List<String> placementContexts) {
        this.placementContexts = String.join(",", placementContexts);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("placement_contexts", placementContexts);
    }
}
