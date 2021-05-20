package com.proptechos.service.filters.buildingcomponent;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.util.List;

/**
 * RoomTypesFilter class for filtering by room types
 *
 * @apiNote Example: 'DataServerRoom,Entrance' - Applicable to RoomService
 * @see com.proptechos.service.RoomService
 */
public class RoomTypesFilter implements IQueryFilter {

    private final String roomTypes;

    public RoomTypesFilter(List<String> roomTypes) {
        this.roomTypes = String.join(",", roomTypes);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("room_types", roomTypes);
    }

}
