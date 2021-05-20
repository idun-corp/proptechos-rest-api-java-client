package com.proptechos.service.filters.common;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

/**
 * AliasNamespaceFilter class for filtering by alias namespace id list
 *
 * @apiNote Example: '2d75379f-89b2-4efa-aa9d-40a799c70786','e618ef09-2113-4e9d-8025-769bcd320466'
 * <p>
 * Not applicable to ActuationInterfaceService, AliasNamespaceService
 */
public class AliasNamespaceFilter implements IQueryFilter {

    private final String aliasNsIds;

    public AliasNamespaceFilter(String aliasNsIds) {
        this.aliasNsIds = aliasNsIds;
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("alias_ns_ids", aliasNsIds);
    }
}
