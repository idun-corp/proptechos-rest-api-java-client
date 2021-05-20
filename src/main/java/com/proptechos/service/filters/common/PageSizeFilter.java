package com.proptechos.service.filters.common;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

/**
 * PageSizeFilter class for filtering by page size
 *
 * @apiNote Example: '50' - Applicable only to PagedService
 */
public class PageSizeFilter implements IQueryFilter {

    private final long pageSize;

    public PageSizeFilter(long pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("size", String.valueOf(pageSize));
    }
}
