package com.proptechos.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockExtension implements BeforeAllCallback, AfterAllCallback {

    private WireMockServer wireMockServer;

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        wireMockServer.resetAll();
        wireMockServer.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        wireMockServer = new WireMockServer(options().port(9090));
        wireMockServer.start();
        WireMock.configureFor("127.0.0.1", wireMockServer.port());
    }
}
