package com.proptechos.service.filters.device;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * DeviceIdsFilter class for filtering by device ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable only to DeviceService
 * @see com.proptechos.service.DeviceService
 */
public class DeviceIdsFilter implements IQueryFilter {

    private final String deviceIds;

    public DeviceIdsFilter(List<String> deviceIds) {
        this.deviceIds = String.join(",", deviceIds);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("device_ids", deviceIds);
    }

}
