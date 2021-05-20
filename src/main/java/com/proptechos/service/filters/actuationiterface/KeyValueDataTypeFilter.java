package com.proptechos.service.filters.actuationiterface;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;
import com.proptechos.model.actuation.DataType;

import java.util.List;

/**
 * KeyValueDataTypeFilter class for filtering actuation interfaces by data types
 *
 * @apiNote Example: 'Integer,Float' - Applicable to ActuationInterfaceService
 * <p>
 * (Possible values: [ Integer | Float | String | Boolean ])
 * @see com.proptechos.service.ActuationInterfaceService
 */
public class KeyValueDataTypeFilter implements IQueryFilter {

    private final List<String> dataTypes;

    public KeyValueDataTypeFilter(List<String> dataTypes) {
        this.dataTypes = dataTypes;
    }

    @Override
    public QueryParam queryParam() {
        for (String dataType : dataTypes) {
            DataType.valueOf(dataType); // to check for valid DataType
        }
        return new QueryParam("key_value_data_types", String.join(",", dataTypes));
    }

}
