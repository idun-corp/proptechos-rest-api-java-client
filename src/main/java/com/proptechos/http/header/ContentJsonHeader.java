package com.proptechos.http.header;

import org.apache.http.client.methods.HttpRequestBase;

public class ContentJsonHeader implements IHeaderHandler {

    @Override
    public void apply(HttpRequestBase requestBase) {
        requestBase.setHeader("Content-Type", "application/json");
    }
}
