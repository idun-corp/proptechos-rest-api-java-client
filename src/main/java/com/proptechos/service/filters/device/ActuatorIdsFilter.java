package com.proptechos.service.filters.device;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * ActuatorIdsFilter class for filtering by actuator ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to ActuatorService
 * @see com.proptechos.service.ActuatorService
 */
public class ActuatorIdsFilter implements IQueryFilter {

    private final String actuatorIds;

    public ActuatorIdsFilter(List<String> actuatorIds) {
        this.actuatorIds = String.join(",", actuatorIds);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("actuator_ids", actuatorIds);
    }

}
