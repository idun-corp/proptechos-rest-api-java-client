package com.proptechos.http.header;

import org.apache.http.client.methods.HttpRequestBase;

public interface IHeaderHandler {

  void apply(HttpRequestBase requestBase);

}
