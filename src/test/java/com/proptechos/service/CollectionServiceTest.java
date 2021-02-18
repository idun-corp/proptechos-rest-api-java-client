package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.COLLECTION_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildCollection;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.Collection;
import com.proptechos.model.common.Paged;
import com.proptechos.utils.DataLoader;
import java.util.List;
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
public class CollectionServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedCollection();
  private static final String TEST_COLLECTION_ID = "1b5d06c9-9f43-46b0-93f4-e9e5d9e3c972";
  private static final Collection TEST_COLLECTION = buildCollection();
  private static final UUID[] TEST_ID_LIST = new UUID[] {
      UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()
  };

  private static CollectionService collectionService;
  private final MockServerClient client;

  CollectionServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    collectionService = serviceFactory.collectionService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + COLLECTION_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + COLLECTION_JSON + "/" + TEST_COLLECTION_ID));
    client.clear(
        request().withPath(APP_CONTEXT + COLLECTION_JSON + "/" + TEST_COLLECTION_ID + "/.*"));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(COLLECTION_JSON)).respond(okResponse(PAGED_DATA));

    collectionService.getFirstPage(15);

    verifyGetRequest(client, COLLECTION_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(COLLECTION_JSON)).respond(okResponse(PAGED_DATA));

    collectionService.getLastPage(15);

    verifyGetRequest(client, COLLECTION_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, COLLECTION_JSON,
        Parameter.param("page", "2"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(COLLECTION_JSON)).respond(okResponse(PAGED_DATA));

    collectionService.getPage(1, 15);

    verifyGetRequest(client, COLLECTION_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(COLLECTION_JSON)).respond(okResponse(PAGED_DATA));

    Paged<Collection> paged = collectionService.getFirstPage(15);
    collectionService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, COLLECTION_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, COLLECTION_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(COLLECTION_JSON + "/" + TEST_COLLECTION_ID))
        .respond(okResponse(DataLoader.loadSingleCollection()));

    collectionService.getById(UUID.fromString(TEST_COLLECTION_ID));

    verifyGetRequest(client, COLLECTION_JSON + "/" + TEST_COLLECTION_ID);
  }

  @Test
  void testGetIncludedAxioms() {
    String requestPath = COLLECTION_JSON + "/" + TEST_COLLECTION_ID + "/includes";
    client.when(getRequest(requestPath))
        .respond(okResponse(DataLoader.loadPagedDevices()));

    collectionService.getIncludedAxioms(
        UUID.fromString(TEST_COLLECTION_ID), 0, 10);

    verifyGetRequest(client, requestPath,
        Parameter.param("page", "0"),
        Parameter.param("size", "10"));
  }

  @Test
  void testCreate() {
    client.when(postRequest(COLLECTION_JSON))
        .respond(okResponse(objectToJson(TEST_COLLECTION)));

    collectionService.createCollection(TEST_COLLECTION);

    verifyPostRequest(client, COLLECTION_JSON, objectToJson(TEST_COLLECTION));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(COLLECTION_JSON))
        .respond(okResponse(objectToJson(TEST_COLLECTION)));

    collectionService.updateCollection(TEST_COLLECTION);

    verifyPutRequest(client, COLLECTION_JSON, objectToJson(TEST_COLLECTION));
  }

  @Test
  void testIncludeAxioms() {
    String requestPath = COLLECTION_JSON + "/" + TEST_COLLECTION_ID + "/include";
    client.when(putRequest(requestPath))
        .respond(okResponse(objectToJson(TEST_ID_LIST)));

    List<UUID> included = collectionService.includeAxioms(UUID.fromString(TEST_COLLECTION_ID),
        TEST_ID_LIST[0], TEST_ID_LIST[1], TEST_ID_LIST[2], TEST_ID_LIST[3], TEST_ID_LIST[4]);

    assertEquals(5, included.size());

    verifyPutRequest(client, requestPath, objectToJson(TEST_ID_LIST));
  }

  @Test
  void testExcludeAxioms() {
    String requestPath = COLLECTION_JSON + "/" + TEST_COLLECTION_ID + "/exclude";
    client.when(putRequest(requestPath))
        .respond(okResponse(objectToJson(TEST_ID_LIST)));

    List<UUID> included = collectionService.excludeAxioms(UUID.fromString(TEST_COLLECTION_ID),
        TEST_ID_LIST[0], TEST_ID_LIST[1], TEST_ID_LIST[2], TEST_ID_LIST[3], TEST_ID_LIST[4]);

    assertEquals(5, included.size());

    verifyPutRequest(client, requestPath, objectToJson(TEST_ID_LIST));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(COLLECTION_JSON + "/" + TEST_COLLECTION_ID))
        .respond(response().withStatusCode(OK));

    collectionService.deleteCollection(UUID.fromString(TEST_COLLECTION_ID));

    verifyDeleteRequest(client, COLLECTION_JSON + "/" + TEST_COLLECTION_ID);
  }
}
