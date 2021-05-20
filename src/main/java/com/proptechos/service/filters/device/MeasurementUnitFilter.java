package com.proptechos.service.filters.device;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * MeasurementUnitFilter class for filtering by measurement unit names
 *
 * @apiNote Example: 'Kilogram,Pascal' - Applicable to DeviceService, ActuatorService, SensorService
 * @see com.proptechos.service.DeviceService
 * @see com.proptechos.service.ActuatorService
 * @see com.proptechos.service.SensorService
 */
public class MeasurementUnitFilter implements IQueryFilter {

    private final String measurementUnits;

    public MeasurementUnitFilter(List<String> measurementUnits) {
        this.measurementUnits = String.join(",", measurementUnits);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("measurement_units", measurementUnits);
    }
}
