package com.proptechos.service.filters.buildingcomponent;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * StoreyIdsFilter class for filtering by storey ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to StoreyService
 * @see com.proptechos.service.StoreyService
 */
public class StoreyIdsFilter implements IQueryFilter {

    private final String storeyIds;

    public StoreyIdsFilter(List<String> storeyIds) {
        this.storeyIds = String.join(",", storeyIds);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("storey_ids", storeyIds);
    }
}
