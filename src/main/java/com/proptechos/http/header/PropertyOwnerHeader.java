package com.proptechos.http.header;

import com.proptechos.auth.PropertyOwnerCache;
import org.apache.http.client.methods.HttpRequestBase;

public class PropertyOwnerHeader implements IHeaderHandler {

  @Override
  public void apply(HttpRequestBase requestBase) {
    if (PropertyOwnerCache.getInstance().getPropertyOwner() != null) {
      requestBase.setHeader("X-Property-Owner",
          PropertyOwnerCache.getInstance().getPropertyOwner());
    }
  }
}
