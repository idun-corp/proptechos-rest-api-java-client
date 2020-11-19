package com.proptechos.service.filters.common;

import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryParam;

/**
 * AliasFilter class for filtering by full littera or by littera part
 *
 * @apiNote Example: '/AS01/GW21/Z211/R4x/FLK1','/GW21/Z211'
 *
 * Not applicable to ActuationInterfaceService, AliasNamespaceService
 */
public class LitteraFilter implements IQueryFilter {

  private final String littera;

  public LitteraFilter(String littera) {
    this.littera = littera;
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("littera", littera);
  }
}
