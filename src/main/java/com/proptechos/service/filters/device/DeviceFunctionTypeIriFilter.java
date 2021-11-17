package com.proptechos.service.filters.device;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * QuantityKindFilter class for filtering by device function types
 *
 * @apiNote Example: 'https://w3id.org/rec/device/Elevator,https://w3id.org/rec/device/Thermostat' - Applicable to DeviceService
 * @see com.proptechos.service.DeviceService
 */
public class DeviceFunctionTypeIriFilter implements IQueryFilter {

    private final String deviceFunctions;

    public DeviceFunctionTypeIriFilter(List<String> deviceFunctions) {
        this.deviceFunctions = String.join(",", deviceFunctions);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("device_function_iris’", deviceFunctions);
    }

}
