package com.proptechos.service.filters.device;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * QuantityKindFilter class for filtering by device function types
 *
 * @apiNote Example: 'Elevator,Thermostat' - Applicable to DeviceService
 * @see com.proptechos.service.DeviceService
 */
public class DeviceFunctionTypeFilter implements IQueryFilter {

    private final String deviceFunctions;

    public DeviceFunctionTypeFilter(List<String> deviceFunctions) {
        this.deviceFunctions = String.join(",", deviceFunctions);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("device_functions", deviceFunctions);
    }

}
