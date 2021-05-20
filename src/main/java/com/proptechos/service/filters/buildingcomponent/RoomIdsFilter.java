package com.proptechos.service.filters.buildingcomponent;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * RoomIdsFilter class for filtering by room ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to RoomService
 * @see com.proptechos.service.RoomService
 */
public class RoomIdsFilter implements IQueryFilter {

    private final String roomIds;

    public RoomIdsFilter(List<String> roomIds) {
        this.roomIds = String.join(",", roomIds);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("room_ids", roomIds);
    }

}
