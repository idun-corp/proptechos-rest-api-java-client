package com.proptechos.service.filters.device;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * SensorIdsFilter class for filtering by sensor ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to SensorService
 * @see com.proptechos.service.SensorService
 */
public class SensorIdsFilter implements IQueryFilter {

    private final String sensorIds;

    public SensorIdsFilter(List<String> sensorIds) {
        this.sensorIds = String.join(",", sensorIds);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("sensor_ids", sensorIds);
    }

}
