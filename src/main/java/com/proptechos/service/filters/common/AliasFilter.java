package com.proptechos.service.filters.common;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

/**
 * AliasFilter class for filtering by alias list
 *
 * @apiNote Example: 'https://test-ns-1.com/alias-1','https://test-ns-2.com/alias-2'
 * <p>
 * Not applicable to ActuationInterfaceService, AliasNamespaceService
 */
public class AliasFilter implements IQueryFilter {

    private final String aliasIds;

    public AliasFilter(String aliasIds) {
        this.aliasIds = aliasIds;
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("alias_ids", aliasIds);
    }
}
