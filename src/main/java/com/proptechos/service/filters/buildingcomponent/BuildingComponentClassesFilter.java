package com.proptechos.service.filters.buildingcomponent;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * BuildingComponentClassesFilter class for filtering by real estate core classes
 *
 * @apiNote Example: 'Room,Storey' - Applicable to BuildingComponentService, DeviceService
 * @see com.proptechos.service.BuildingComponentService
 * @see com.proptechos.service.DeviceService
 */
public class BuildingComponentClassesFilter implements IQueryFilter {

    private final String recClasses;

    public BuildingComponentClassesFilter(List<String> recClasses) {
        this.recClasses = String.join(",", recClasses);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("buildingcomponent_classes", recClasses);
    }

}
