package com.proptechos.service;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.ProptechOsClient;
import com.proptechos.http.constants.HttpStatus;
import com.proptechos.model.auth.AuthenticationConfig;
import java.io.InputStream;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.opentest4j.AssertionFailedError;

public class BaseServiceTest {

  public static final String APP_CONTEXT = "/api";
  protected static ServiceFactory serviceFactory;

  private static final Properties props = new Properties();

  @BeforeAll
  static void setUpProptechOsClient() {
    loadConfig();
    ProptechOsClient client = ProptechOsClient
        .applicationClientBuilder("http://localhost:8080/api").authConfig(
            AuthenticationConfig.builder()
                .clientId(props.getProperty("test.client.app.id"))
                .clientSecret(props.getProperty("test.client.app.secret"))
                .build()).build();
    serviceFactory = client.serviceFactory();
  }

  HttpRequest getRequest(String path) {
    return request()
        .withMethod(HttpMethod.GET.name())
        .withPath(APP_CONTEXT + path);
  }

  HttpRequest postRequest(String path) {
    return request()
        .withMethod(HttpMethod.POST.name())
        .withPath(APP_CONTEXT + path);
  }

  HttpRequest putRequest(String path) {
    return request()
        .withMethod(HttpMethod.PUT.name())
        .withPath(APP_CONTEXT + path);
  }

  HttpRequest deleteRequest(String path) {
    return request()
        .withMethod(HttpMethod.DELETE.name())
        .withPath(APP_CONTEXT + path);
  }

  HttpResponse okResponse(String body) {
    return response()
        .withStatusCode(HttpStatus.OK)
        .withContentType(MediaType.APPLICATION_JSON)
        .withBody(body);
  }

  private static void loadConfig() {
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

  public enum HttpMethod {
    GET, POST, PUT, DELETE
  }

}
