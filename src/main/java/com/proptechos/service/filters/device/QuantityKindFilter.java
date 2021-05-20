package com.proptechos.service.filters.device;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * QuantityKindFilter class for filtering by quantity kind names
 *
 * @apiNote Example: 'Humidity,Length' - Applicable to DeviceService, ActuatorService, SensorService
 * @see com.proptechos.service.DeviceService
 * @see com.proptechos.service.ActuatorService
 * @see com.proptechos.service.SensorService
 */
public class QuantityKindFilter implements IQueryFilter {

    private final String quantityKinds;

    public QuantityKindFilter(List<String> quantityKinds) {
        this.quantityKinds = String.join(",", quantityKinds);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("quantity_kinds", quantityKinds);
    }
}
