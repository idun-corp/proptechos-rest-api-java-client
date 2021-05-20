package com.proptechos.utils;

import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.proptechos.service.BaseServiceTest;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ValidationUtils {

    public static void verifyGetRequest(String path, Map<String, String> parameters) {
        RequestPatternBuilder builder =
            getRequestedFor(urlPathMatching(BaseServiceTest.APP_CONTEXT + path))
                .withHeader("Accept", equalTo("application/json"));
        parameters.forEach((key, value) -> builder.withQueryParam(key, equalTo(value)));
        verify(builder);
    }

    public static void verifyPostRequest(String path, String jsonBody) {
        RequestPatternBuilder builder =
            postRequestedFor(urlPathMatching(BaseServiceTest.APP_CONTEXT + path))
                .withHeader("Accept", equalTo("application/json"))
                .withRequestBody(equalToJson(jsonBody));
        verify(builder);
    }

    public static void verifyPutRequest(String path, String jsonBody) {
        RequestPatternBuilder builder =
            putRequestedFor(urlPathMatching(BaseServiceTest.APP_CONTEXT + path))
                .withHeader("Accept", equalTo("application/json"))
                .withRequestBody(equalToJson(jsonBody));
        verify(builder);
    }

    public static void verifyDeleteRequest(String path) {
        verify(deleteRequestedFor(urlPathMatching(BaseServiceTest.APP_CONTEXT + path)));
    }

}
