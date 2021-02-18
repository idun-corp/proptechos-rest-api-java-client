package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ASSET_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildAsset;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.Asset;
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
public class AssetServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedAssets();
  private static final String TEST_ASSET_ID = "3f065d63-7d95-417d-9b24-19647294aa9c";
  private static final Asset TEST_ASSET = buildAsset();

  private static AssetService assetService;
  private final MockServerClient client;

  AssetServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    assetService = serviceFactory.assetService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + ASSET_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + ASSET_JSON + "/" + TEST_ASSET_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(ASSET_JSON)).respond(okResponse(PAGED_DATA));

    assetService.getFirstPage(15);

    verifyGetRequest(client, ASSET_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(ASSET_JSON)).respond(okResponse(PAGED_DATA));

    assetService.getLastPage(15);

    verifyGetRequest(client, ASSET_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, ASSET_JSON,
        Parameter.param("page", "4"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(ASSET_JSON)).respond(okResponse(PAGED_DATA));

    assetService.getPage(1, 15);

    verifyGetRequest(client, ASSET_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(ASSET_JSON)).respond(okResponse(PAGED_DATA));

    Paged<Asset> paged = assetService.getFirstPage(15);
    assetService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, ASSET_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, ASSET_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(ASSET_JSON + "/" + TEST_ASSET_ID))
        .respond(okResponse(DataLoader.loadSingleAsset()));

    assetService.getById(UUID.fromString(TEST_ASSET_ID));

    verifyGetRequest(client, ASSET_JSON + "/" + TEST_ASSET_ID);
  }

  @Test
  void testCreate() {
    client.when(postRequest(ASSET_JSON))
        .respond(okResponse(objectToJson(TEST_ASSET)));

    assetService.createAsset(TEST_ASSET);

    verifyPostRequest(client, ASSET_JSON, objectToJson(TEST_ASSET));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(ASSET_JSON))
        .respond(okResponse(objectToJson(TEST_ASSET)));

    assetService.updateAsset(TEST_ASSET);

    verifyPutRequest(client, ASSET_JSON, objectToJson(TEST_ASSET));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(ASSET_JSON + "/" + TEST_ASSET_ID))
        .respond(response().withStatusCode(OK));

    assetService.deleteAsset(UUID.fromString(TEST_ASSET_ID));

    verifyDeleteRequest(client, ASSET_JSON + "/" + TEST_ASSET_ID);
  }
}
