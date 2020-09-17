package com.proptechos.http.header;

import static com.proptechos.http.constants.MediaType.APPLICATION_JSONLD;

import org.apache.http.client.methods.HttpRequestBase;

public class AcceptJsonLdHeader implements IHeaderHandler {

  @Override
  public void apply(HttpRequestBase requestBase) {
    requestBase.setHeader("Accept", APPLICATION_JSONLD);
  }
}
