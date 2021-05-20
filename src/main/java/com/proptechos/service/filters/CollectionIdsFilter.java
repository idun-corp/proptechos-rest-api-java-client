package com.proptechos.service.filters;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * CollectionIdsFilter class for filtering by asset ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to CollectionService
 * @see com.proptechos.service.CollectionService
 */
public class CollectionIdsFilter implements IQueryFilter {

    private final String collectionIds;

    public CollectionIdsFilter(List<String> collectionIds) {
        this.collectionIds = String.join(",", collectionIds);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("collection_ids", collectionIds);
    }
}
