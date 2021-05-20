package com.proptechos.service.filters.device;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * DeviceClassesFilter class for filtering by real estate core classes
 *
 * @apiNote Example: 'Sensor,Device' - Applicable only to DeviceService
 * @see com.proptechos.service.DeviceService
 */
public class DeviceClassesFilter implements IQueryFilter {

    private final String recClasses;

    public DeviceClassesFilter(List<String> recClasses) {
        this.recClasses = String.join(",", recClasses);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("device_classes", recClasses);
    }
}
