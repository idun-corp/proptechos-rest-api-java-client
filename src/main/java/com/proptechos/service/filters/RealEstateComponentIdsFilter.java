package com.proptechos.service.filters;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * RealEstateComponentIdsFilter class for filtering by real estate component ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to
 * RealEstateComponentService,
 * @see com.proptechos.service.RealEstateComponentService
 */
public class RealEstateComponentIdsFilter implements IQueryFilter {

    private final String realEstateComponentIds;

    public RealEstateComponentIdsFilter(List<String> buildingIds) {
        this.realEstateComponentIds = String.join(",", buildingIds);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("realestatecomponent_ids", realEstateComponentIds);
    }
}
