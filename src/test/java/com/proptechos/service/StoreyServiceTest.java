package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.STOREY_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildStorey;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.buildingcomponent.Storey;
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
public class StoreyServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedStoreys();
  private static final String TEST_STOREY_ID = "d92a2a9f-6a4c-4cf0-9ddc-43f5666ebe37";
  private static final Storey TEST_STOREY = buildStorey();

  private static StoreyService storeyService;
  private final MockServerClient client;
  
  StoreyServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    storeyService = serviceFactory.storeyService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + STOREY_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + STOREY_JSON + "/" + TEST_STOREY_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(STOREY_JSON)).respond(okResponse(PAGED_DATA));

    storeyService.getFirstPage(15);

    verifyGetRequest(client, STOREY_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(STOREY_JSON)).respond(okResponse(PAGED_DATA));

    storeyService.getLastPage(15);

    verifyGetRequest(client, STOREY_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, STOREY_JSON,
        Parameter.param("page", "4"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(STOREY_JSON)).respond(okResponse(PAGED_DATA));

    storeyService.getPage(1, 15);

    verifyGetRequest(client, STOREY_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(STOREY_JSON)).respond(okResponse(PAGED_DATA));

    Paged<Storey> paged = storeyService.getFirstPage(15);
    storeyService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, STOREY_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, STOREY_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(STOREY_JSON + "/" + TEST_STOREY_ID))
        .respond(okResponse(DataLoader.loadSingleStorey()));

    storeyService.getById(UUID.fromString(TEST_STOREY_ID));

    verifyGetRequest(client, STOREY_JSON + "/" + TEST_STOREY_ID);
  }

  @Test
  void testCreate() {
    client.when(postRequest(STOREY_JSON))
        .respond(okResponse(objectToJson(TEST_STOREY)));

    storeyService.createStorey(TEST_STOREY);

    verifyPostRequest(client, STOREY_JSON, objectToJson(TEST_STOREY));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(STOREY_JSON))
        .respond(okResponse(objectToJson(TEST_STOREY)));

    storeyService.updateStorey(TEST_STOREY);

    verifyPutRequest(client, STOREY_JSON, objectToJson(TEST_STOREY));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(STOREY_JSON + "/" + TEST_STOREY_ID))
        .respond(response().withStatusCode(OK));

    storeyService.deleteStorey(UUID.fromString(TEST_STOREY_ID));

    verifyDeleteRequest(client, STOREY_JSON + "/" + TEST_STOREY_ID);
  }
}
