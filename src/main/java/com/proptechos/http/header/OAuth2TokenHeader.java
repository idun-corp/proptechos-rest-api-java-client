package com.proptechos.http.header;

import com.proptechos.auth.AccessTokenCache;
import org.apache.http.client.methods.HttpRequestBase;

public class OAuth2TokenHeader implements IHeaderHandler {

  private final AccessTokenCache tokenCache;

  public OAuth2TokenHeader() {
    this.tokenCache = AccessTokenCache.getInstance();
  }

  @Override
  public void apply(HttpRequestBase requestBase) {
    requestBase.setHeader("Authorization", "Bearer " + tokenCache.authResult().accessToken());
  }
}
