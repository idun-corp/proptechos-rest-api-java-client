package com.proptechos.service.filters.twin;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

/**
 * CommonFieldsFilter class used define whether twin should return only common fields or all fields
 *
 * @apiNote Applicable only to TwinService
 * @see com.proptechos.service.TwinService
 */
public class CommonFieldsFilter implements IQueryFilter {

    private final boolean onlyCommonFields;

    public CommonFieldsFilter(boolean onlyCommonFields) {
        this.onlyCommonFields = onlyCommonFields;
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("only_common_fields", String.valueOf(onlyCommonFields));
    }
}
