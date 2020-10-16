package com.proptechos.utils;

import static org.mockserver.model.HttpRequest.request;

import com.proptechos.service.BaseServiceTest;
import com.proptechos.service.BaseServiceTest.HttpMethod;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.JsonBody;
import org.mockserver.model.MediaType;
import org.mockserver.model.Parameter;
import org.mockserver.verify.VerificationTimes;

public class ValidationUtils {

  public static void verifyGetRequest(MockServerClient client, String path, Parameter...parameters) {
    client.verify(request()
            .withMethod(HttpMethod.GET.name())
            .withHeader("Authorization")
            .withHeader("Accept", "application/json")
            .withPath(BaseServiceTest.APP_CONTEXT + path)
            .withQueryStringParameters(parameters),
        VerificationTimes.once());
  }

  public static void verifyPostRequest(MockServerClient client, String path, String jsonBody) {
    client.verify(request()
            .withMethod(HttpMethod.POST.name())
            .withPath(BaseServiceTest.APP_CONTEXT + path)
            .withHeader("Authorization")
            .withContentType(MediaType.APPLICATION_JSON)
            .withBody(new JsonBody(jsonBody)),
        VerificationTimes.once());
  }

  public static void verifyPutRequest(MockServerClient client, String path, String jsonBody) {
    client.verify(request()
            .withMethod(HttpMethod.PUT.name())
            .withPath(BaseServiceTest.APP_CONTEXT + path)
            .withHeader("Authorization")
            .withContentType(MediaType.APPLICATION_JSON)
            .withBody(new JsonBody(jsonBody)),
        VerificationTimes.once());
  }

  public static void verifyDeleteRequest(MockServerClient client, String path) {
    client.verify(request()
            .withMethod(HttpMethod.DELETE.name())
            .withHeader("Authorization")
            .withPath(BaseServiceTest.APP_CONTEXT + path),
        VerificationTimes.once());
  }

}
