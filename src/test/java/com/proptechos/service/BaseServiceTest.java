package com.proptechos.service;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.proptechos.ProptechOsClient;
import com.proptechos.model.auth.AuthenticationConfig;
import org.junit.jupiter.api.BeforeAll;
import org.opentest4j.AssertionFailedError;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class BaseServiceTest {

    public static final String APP_CONTEXT = "/api";
    protected static ServiceFactory serviceFactory;

    private static final Properties props = new Properties();

    @BeforeAll
    static void setUpEnvironment() {
        loadConfig();
        ProptechOsClient client = ProptechOsClient
            .applicationClientBuilder("http://localhost:9090/api").authConfig(
                AuthenticationConfig.builder()
                    .clientId(props.getProperty("test.client.app.id"))
                    .clientSecret(props.getProperty("test.client.app.secret"))
                    .build()).build();
        serviceFactory = client.serviceFactory();
    }

    static void stubGetResponse(String path, Map<String, String> parameters, String response) {
        MappingBuilder builder = stubResponse(get(urlPathMatching(APP_CONTEXT + path)), response);
        parameters.forEach((key, value) -> builder.withQueryParam(key, equalTo(value)));
        stubFor(builder);
    }

    static void stubPostResponse(String path, String response) {
        MappingBuilder builder = stubResponse(post(urlPathMatching(APP_CONTEXT + path)), response);
        stubFor(builder);
    }

    static void stubPutResponse(String path, String response) {
        MappingBuilder builder = stubResponse(put(urlPathMatching(APP_CONTEXT + path)), response);
        stubFor(builder);
    }

    static void stubFailedPostResponse(String path, String response) {
        MappingBuilder builder = stubBadRequestResponse(post(urlPathMatching(APP_CONTEXT + path)), response);
        stubFor(builder);
    }

    static void stubFailedPutResponse(String path, String response) {
        MappingBuilder builder = stubBadRequestResponse(put(urlPathMatching(APP_CONTEXT + path)), response);
        stubFor(builder);
    }

    static void stubDeleteResponse(String path) {
        MappingBuilder builder = delete(urlPathMatching(APP_CONTEXT + path)).withPort(9090)
            .willReturn(aResponse().withStatus(200));
        stubFor(builder);
    }

    private static MappingBuilder stubResponse(MappingBuilder builder, String response) {
        return builder.withPort(9090)
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response));
    }

    private static MappingBuilder stubBadRequestResponse(MappingBuilder builder, String response) {
        return builder.withPort(9090)
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(response));
    }

    static void loadConfig() {
        String configFile = "test.properties";
        try (InputStream resourceAsStream =
                 BaseServiceTest.class.getClassLoader().getResourceAsStream(configFile)) {
            if (resourceAsStream != null) {
                props.load(resourceAsStream);
            } else {
                throw new IllegalArgumentException("Config file is missing or empty");
            }
        } catch (Exception e) {
            throw new AssertionFailedError("Unable to load config file", e);
        }
    }

}
