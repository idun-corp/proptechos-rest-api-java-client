package com.proptechos.service.filters.actuationiterface;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * ActuationInterfaceIdsFilter class for filtering by actuation interface ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to ActuationInterfaceService
 * @see com.proptechos.service.ActuationInterfaceService
 */
public class ActuationInterfaceIdsFilter implements IQueryFilter {

    private final String actuationInterfaceIds;

    public ActuationInterfaceIdsFilter(List<String> actuationInterfaceIds) {
        this.actuationInterfaceIds = String.join(",", actuationInterfaceIds);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("actuation_interface_ids", actuationInterfaceIds);
    }

}
