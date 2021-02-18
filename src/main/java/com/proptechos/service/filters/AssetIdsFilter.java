package com.proptechos.service.filters;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;
import java.util.List;

/**
 * AssetIdsFilter class for filtering by asset ids
 *
 * @apiNote Example: '2becfc1a-7f21-45b9-8255-771d59d145ba,d1aae508-64f1-4942-a0c6-6e3e8d452a36'
 * - Applicable to AssetService, DeviceService, SensorService, ActuatorService
 *
 * @see com.proptechos.service.AssetService
 * @see com.proptechos.service.DeviceService
 * @see com.proptechos.service.SensorService
 * @see com.proptechos.service.ActuatorService
 */
public class AssetIdsFilter implements IQueryFilter {

  private final String assetIds;

  public AssetIdsFilter(List<String> assetIds) {
    this.assetIds = String.join(",", assetIds);
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("asset_ids", assetIds);
  }
}
