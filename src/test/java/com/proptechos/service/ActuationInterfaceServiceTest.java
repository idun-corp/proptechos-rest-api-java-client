package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ACTUATION_INTERFACE_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildActuationInterface;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.actuation.ActuationInterface;
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
@MockServerSettings(ports = {9090})
public class ActuationInterfaceServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedActuationInterfaces();
  private static final String TEST_AI_ID = "c61c5a10-83ac-40b8-8f53-2e4b2f6df801";
  private static final ActuationInterface TEST_ACTUATION_INTERFACE = buildActuationInterface();

  private static ActuationInterfaceService actuationInterfaceService;
  private final MockServerClient client;
  
  ActuationInterfaceServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    actuationInterfaceService = serviceFactory.actuationInterfaceService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + ACTUATION_INTERFACE_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + ACTUATION_INTERFACE_JSON + "/" + TEST_AI_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(ACTUATION_INTERFACE_JSON)).respond(okResponse(PAGED_DATA));

    actuationInterfaceService.getFirstPage(5);

    verifyGetRequest(client, ACTUATION_INTERFACE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "5"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(ACTUATION_INTERFACE_JSON)).respond(okResponse(PAGED_DATA));

    actuationInterfaceService.getLastPage(5);

    verifyGetRequest(client, ACTUATION_INTERFACE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "5"));

    verifyGetRequest(client, ACTUATION_INTERFACE_JSON,
        Parameter.param("page", "2"),
        Parameter.param("size", "5"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(ACTUATION_INTERFACE_JSON)).respond(okResponse(PAGED_DATA));

    actuationInterfaceService.getPage(1, 5);

    verifyGetRequest(client, ACTUATION_INTERFACE_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "5"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(ACTUATION_INTERFACE_JSON)).respond(okResponse(PAGED_DATA));

    Paged<ActuationInterface> paged = actuationInterfaceService.getFirstPage(5);
    actuationInterfaceService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, ACTUATION_INTERFACE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "5"));

    verifyGetRequest(client, ACTUATION_INTERFACE_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "5"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(ACTUATION_INTERFACE_JSON + "/" + TEST_AI_ID))
        .respond(okResponse(DataLoader.loadSingleActuationInterface()));

    actuationInterfaceService.getById(UUID.fromString(TEST_AI_ID));

    verifyGetRequest(client, ACTUATION_INTERFACE_JSON + "/" + TEST_AI_ID);
  }


  @Test
  void testCreate() {
    client.when(postRequest(ACTUATION_INTERFACE_JSON))
        .respond(okResponse(objectToJson(TEST_ACTUATION_INTERFACE)));

    actuationInterfaceService
        .createActuationInterface(TEST_ACTUATION_INTERFACE);

    verifyPostRequest(
        client, ACTUATION_INTERFACE_JSON, objectToJson(TEST_ACTUATION_INTERFACE));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(ACTUATION_INTERFACE_JSON))
        .respond(okResponse(objectToJson(TEST_ACTUATION_INTERFACE)));

    actuationInterfaceService
        .updateActuationInterface(TEST_ACTUATION_INTERFACE);

    verifyPutRequest(
        client, ACTUATION_INTERFACE_JSON, objectToJson(TEST_ACTUATION_INTERFACE));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(ACTUATION_INTERFACE_JSON + "/" + TEST_AI_ID))
        .respond(response().withStatusCode(OK));

    actuationInterfaceService
        .deleteActuationInterface(UUID.fromString(TEST_AI_ID));

    verifyDeleteRequest(client, ACTUATION_INTERFACE_JSON + "/" + TEST_AI_ID);
  }
}
