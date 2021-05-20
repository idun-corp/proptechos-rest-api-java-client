package com.proptechos.http.header;

import static com.proptechos.http.constants.MediaType.APPLICATION_JSON;

import org.apache.http.client.methods.HttpRequestBase;

public class AcceptJsonHeader implements IHeaderHandler {

    @Override
    public void apply(HttpRequestBase requestBase) {
        requestBase.setHeader("Accept", APPLICATION_JSON);
    }
}
