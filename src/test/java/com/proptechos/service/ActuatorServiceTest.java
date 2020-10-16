package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ACTUATOR_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildActuator;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.Actuator;
import com.proptechos.model.common.Paged;
import com.proptechos.utils.DataLoader;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.Parameter;

@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = {8080})
public class ActuatorServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedActuators();
  private static final String TEST_ACTUATOR_ID = "846f0b95-5c26-4cda-a236-0b35f1d362e3";
  private static final Actuator TEST_ACTUATOR = buildActuator();

  private static ActuatorService actuatorService;
  private final MockServerClient client;
  
  ActuatorServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    actuatorService = serviceFactory.actuatorService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + ACTUATOR_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + ACTUATOR_JSON + "/" + TEST_ACTUATOR_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(ACTUATOR_JSON)).respond(okResponse(PAGED_DATA));

    actuatorService.getFirstPage(15);

    verifyGetRequest(client, ACTUATOR_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(ACTUATOR_JSON)).respond(okResponse(PAGED_DATA));

    actuatorService.getLastPage(15);

    verifyGetRequest(client, ACTUATOR_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, ACTUATOR_JSON,
        Parameter.param("page", "10"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(ACTUATOR_JSON)).respond(okResponse(PAGED_DATA));

    actuatorService.getPage(1, 15);

    verifyGetRequest(client, ACTUATOR_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(ACTUATOR_JSON)).respond(okResponse(PAGED_DATA));

    Paged<Actuator> paged = actuatorService.getFirstPage(15);
    actuatorService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, ACTUATOR_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, ACTUATOR_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(ACTUATOR_JSON + "/" + TEST_ACTUATOR_ID))
        .respond(okResponse(DataLoader.loadSingleActuator()));

    actuatorService.getById(UUID.fromString(TEST_ACTUATOR_ID));

    verifyGetRequest(client, ACTUATOR_JSON + "/" + TEST_ACTUATOR_ID);
  }

  @Test
  void testCreate() {
    client.when(postRequest(ACTUATOR_JSON))
        .respond(okResponse(objectToJson(TEST_ACTUATOR)));

    actuatorService.createActuator(TEST_ACTUATOR);

    verifyPostRequest(client, ACTUATOR_JSON, objectToJson(TEST_ACTUATOR));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(ACTUATOR_JSON))
        .respond(okResponse(objectToJson(TEST_ACTUATOR)));

    actuatorService.updateActuator(TEST_ACTUATOR);

    verifyPutRequest(client, ACTUATOR_JSON, objectToJson(TEST_ACTUATOR));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(ACTUATOR_JSON + "/" + TEST_ACTUATOR_ID))
        .respond(response().withStatusCode(OK));

    actuatorService.deleteActuator(UUID.fromString(TEST_ACTUATOR_ID));

    verifyDeleteRequest(client, ACTUATOR_JSON + "/" + TEST_ACTUATOR_ID);
  }

}
