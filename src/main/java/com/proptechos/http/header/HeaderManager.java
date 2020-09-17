package com.proptechos.http.header;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.client.methods.HttpRequestBase;

public class HeaderManager {

  private final List<IHeaderHandler> headerHandlers;

  private HeaderManager() {
    this.headerHandlers = new LinkedList<>();
  }

  public void registerHandler(IHeaderHandler headerHandler) {
    this.headerHandlers.add(headerHandler);
  }

  public void addHeaders(HttpRequestBase requestBase) {
    headerHandlers.forEach(header -> header.apply(requestBase));
  }

  public static HeaderManager getInstance(IHeaderHandler...headers) {
    HeaderManager headerManager = new HeaderManager();
    if (headers.length > 0) {
      headerManager.headerHandlers.addAll(Arrays.asList(headers));
    }
    return headerManager;
  }

}
