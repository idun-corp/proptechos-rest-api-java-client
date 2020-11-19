package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.REAL_ESTATE_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildRealEstate;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.Point;
import com.proptechos.model.RealEstate;
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
public class RealEstateServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedRealEstates();
  private static final String LIST_DATA = DataLoader.loadRealEstateList();
  private static final String TEST_RE_ID = "1b5d06c9-9f43-46b0-93f4-e9e5d9e3c972";
  private static final RealEstate TEST_RE = buildRealEstate();

  private static RealEstateService realEstateService;
  private final MockServerClient client;

  RealEstateServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    realEstateService = serviceFactory.realEstateService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + REAL_ESTATE_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + REAL_ESTATE_JSON + "/inrange"));
    client.clear(
        request().withPath(APP_CONTEXT + REAL_ESTATE_JSON + "/" + TEST_RE_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(REAL_ESTATE_JSON)).respond(okResponse(PAGED_DATA));

    realEstateService.getFirstPage(15);

    verifyGetRequest(client, REAL_ESTATE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(REAL_ESTATE_JSON)).respond(okResponse(PAGED_DATA));

    realEstateService.getLastPage(15);

    verifyGetRequest(client, REAL_ESTATE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, REAL_ESTATE_JSON,
        Parameter.param("page", "2"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(REAL_ESTATE_JSON)).respond(okResponse(PAGED_DATA));

    realEstateService.getPage(1, 15);

    verifyGetRequest(client, REAL_ESTATE_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(REAL_ESTATE_JSON)).respond(okResponse(PAGED_DATA));

    Paged<RealEstate> paged = realEstateService.getFirstPage(15);
    realEstateService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, REAL_ESTATE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, REAL_ESTATE_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetInRange() {
    client.when(getRequest(REAL_ESTATE_JSON + "/inrange")).respond(okResponse(LIST_DATA));

    realEstateService.getRealEstatesInRange(new Point(59, 18, 100));

    verifyGetRequest(client, REAL_ESTATE_JSON + "/inrange",
        Parameter.param("lat", "59.0"),
        Parameter.param("lon", "18.0"),
        Parameter.param("dist", "100.0"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(REAL_ESTATE_JSON + "/" + TEST_RE_ID))
        .respond(okResponse(DataLoader.loadSingleRealEstate()));

    realEstateService.getById(UUID.fromString(TEST_RE_ID));

    verifyGetRequest(client, REAL_ESTATE_JSON + "/" + TEST_RE_ID);
  }

  @Test
  void testCreate() {
    client.when(postRequest(REAL_ESTATE_JSON))
        .respond(okResponse(objectToJson(TEST_RE)));

    realEstateService.createRealEstate(TEST_RE);

    verifyPostRequest(client, REAL_ESTATE_JSON, objectToJson(TEST_RE));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(REAL_ESTATE_JSON))
        .respond(okResponse(objectToJson(TEST_RE)));

    realEstateService.updateRealEstate(TEST_RE);

    verifyPutRequest(client, REAL_ESTATE_JSON, objectToJson(TEST_RE));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(REAL_ESTATE_JSON + "/" + TEST_RE_ID))
        .respond(response().withStatusCode(OK));

    realEstateService.deleteRealEstate(UUID.fromString(TEST_RE_ID));

    verifyDeleteRequest(client, REAL_ESTATE_JSON + "/" + TEST_RE_ID);
  }
}
