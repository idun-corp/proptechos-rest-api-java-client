package com.proptechos.service.filters;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * RealEstateComponentClassesFilter class for filtering by real estate component rec classes
 *
 * @apiNote Example: 'Building,Land'
 * If no value is provided 'Building' value will be taken as default on Rest API side
 * - Applicable to
 * RealEstateComponentService,
 * @see com.proptechos.service.RealEstateComponentService
 */
public class RealEstateComponentClassesFilter implements IQueryFilter {

    private final String realEstateComponentClasses;

    public RealEstateComponentClassesFilter(List<String> buildingIds) {
        this.realEstateComponentClasses = String.join(",", buildingIds);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("realestatecomponent_classes", realEstateComponentClasses);
    }

}
