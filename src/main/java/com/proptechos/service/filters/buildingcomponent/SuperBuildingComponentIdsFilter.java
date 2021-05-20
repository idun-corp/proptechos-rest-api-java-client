package com.proptechos.service.filters.buildingcomponent;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * SuperBuildingComponentIdsFilter class for filtering by super building component ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to RoomService, StoreyService, DeviceService, SensorService, ActuatorService
 * @see com.proptechos.service.RoomService
 * @see com.proptechos.service.StoreyService
 * @see com.proptechos.service.DeviceService
 * @see com.proptechos.service.SensorService
 * @see com.proptechos.service.ActuatorService
 */
public class SuperBuildingComponentIdsFilter implements IQueryFilter {

    private final String superBuildingComponentIds;

    public SuperBuildingComponentIdsFilter(List<String> superBuildingComponentIds) {
        this.superBuildingComponentIds = String.join(",", superBuildingComponentIds);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("super_buildingcomponent_ids", superBuildingComponentIds);
    }

}
