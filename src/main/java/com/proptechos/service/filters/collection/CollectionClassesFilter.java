package com.proptechos.service.filters.collection;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * CollectionClassesFilter class for filtering by real estate core classes
 *
 * @apiNote Example: 'System,Collection' - Applicable only to CollectionService
 * @see com.proptechos.service.CollectionService
 */
public class CollectionClassesFilter implements IQueryFilter {

    private final String recClasses;

    public CollectionClassesFilter(List<String> recClasses) {
        this.recClasses = String.join(",", recClasses);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("collection_classes", recClasses);
    }
}
