package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ALIAS_NAMESPACE_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildAliasNamespace;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.AliasNamespace;
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
public class AliasNamespaceServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedAliasNamespaces();
  private static final String TEST_AN_ID = "2afba45c-b50d-4e9b-b230-475b5464eceb";
  private static final AliasNamespace TEST_AN = buildAliasNamespace();

  private static AliasNamespaceService aliasNamespaceService;
  private final MockServerClient client;
  
  AliasNamespaceServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    aliasNamespaceService = serviceFactory.aliasNamespaceService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + ALIAS_NAMESPACE_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + ALIAS_NAMESPACE_JSON + "/" + TEST_AN_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(ALIAS_NAMESPACE_JSON)).respond(okResponse(PAGED_DATA));

    aliasNamespaceService.getFirstPage(5);

    verifyGetRequest(client, ALIAS_NAMESPACE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "5"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(ALIAS_NAMESPACE_JSON)).respond(okResponse(PAGED_DATA));

    aliasNamespaceService.getLastPage(5);

    verifyGetRequest(client, ALIAS_NAMESPACE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "5"));

    verifyGetRequest(client, ALIAS_NAMESPACE_JSON,
        Parameter.param("page", "3"),
        Parameter.param("size", "5"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(ALIAS_NAMESPACE_JSON)).respond(okResponse(PAGED_DATA));

    aliasNamespaceService.getPage(1, 5);

    verifyGetRequest(client, ALIAS_NAMESPACE_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "5"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(ALIAS_NAMESPACE_JSON)).respond(okResponse(PAGED_DATA));

    Paged<AliasNamespace> paged = aliasNamespaceService.getFirstPage(5);
    aliasNamespaceService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, ALIAS_NAMESPACE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "5"));

    verifyGetRequest(client, ALIAS_NAMESPACE_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "5"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(ALIAS_NAMESPACE_JSON + "/" + TEST_AN_ID))
        .respond(okResponse(DataLoader.loadSingleAliasNamespace()));

    aliasNamespaceService.getById(UUID.fromString(TEST_AN_ID));

    verifyGetRequest(client, ALIAS_NAMESPACE_JSON + "/" + TEST_AN_ID);
  }

  @Test
  void testCreate() {
    client.when(postRequest(ALIAS_NAMESPACE_JSON))
        .respond(okResponse(objectToJson(TEST_AN)));

    aliasNamespaceService.createAliasNamespace(TEST_AN);

    verifyPostRequest(client, ALIAS_NAMESPACE_JSON, objectToJson(TEST_AN));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(ALIAS_NAMESPACE_JSON))
        .respond(okResponse(objectToJson(TEST_AN)));

    aliasNamespaceService.updateAliasNamespace(TEST_AN);

    verifyPutRequest(client, ALIAS_NAMESPACE_JSON, objectToJson(TEST_AN));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(ALIAS_NAMESPACE_JSON + "/" + TEST_AN_ID))
        .respond(response().withStatusCode(OK));

    aliasNamespaceService.deleteAliasNamespace(UUID.fromString(TEST_AN_ID));

    verifyDeleteRequest(client, ALIAS_NAMESPACE_JSON + "/" + TEST_AN_ID);
  }

}
