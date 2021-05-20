package com.proptechos.service.filters.device;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * EndTimeFilter class used for receiving observation for period
 *
 * @apiNote Applicable only to SensorService
 * @see com.proptechos.service.SensorService
 */
public class EndTimeFilter implements IQueryFilter {

    private final String endTimeString;

    public EndTimeFilter(Instant endTime) {
        this.endTimeString = DateTimeFormatter.ISO_INSTANT.format(endTime);
    }

    @Override
    public QueryParam queryParam() {
        return new QueryParam("endTime", endTimeString);
    }
}
