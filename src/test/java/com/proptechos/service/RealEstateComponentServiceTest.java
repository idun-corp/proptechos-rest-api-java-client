package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.REAL_ESTATE_COMPONENT_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildBuilding;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.Building;
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
public class RealEstateComponentServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedBuildings();
  private static final String TEST_BUILDING_ID = "79b213c9-57d9-4969-9963-70efd069c0d3";
  private static final Building TEST_BUILDING = buildBuilding();
  
  private static RealEstateComponentService realEstateComponentService;
  private final MockServerClient client;
  
  RealEstateComponentServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    realEstateComponentService = serviceFactory.realEstateComponentService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + REAL_ESTATE_COMPONENT_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + REAL_ESTATE_COMPONENT_JSON + "/" + TEST_BUILDING_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(REAL_ESTATE_COMPONENT_JSON)).respond(okResponse(PAGED_DATA));

    realEstateComponentService.getFirstPage(15);

    verifyGetRequest(client, REAL_ESTATE_COMPONENT_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(REAL_ESTATE_COMPONENT_JSON)).respond(okResponse(PAGED_DATA));

    realEstateComponentService.getLastPage(15);

    verifyGetRequest(client, REAL_ESTATE_COMPONENT_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, REAL_ESTATE_COMPONENT_JSON,
        Parameter.param("page", "3"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(REAL_ESTATE_COMPONENT_JSON)).respond(okResponse(PAGED_DATA));

    realEstateComponentService.getPage(1, 15);

    verifyGetRequest(client, REAL_ESTATE_COMPONENT_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(REAL_ESTATE_COMPONENT_JSON)).respond(okResponse(PAGED_DATA));

    Paged<Building> paged = realEstateComponentService.getFirstPage(15);
    realEstateComponentService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, REAL_ESTATE_COMPONENT_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "15"));

    verifyGetRequest(client, REAL_ESTATE_COMPONENT_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "15"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(REAL_ESTATE_COMPONENT_JSON + "/" + TEST_BUILDING_ID))
        .respond(okResponse(DataLoader.loadSingleBuilding()));

    realEstateComponentService.getById(UUID.fromString(TEST_BUILDING_ID));

    verifyGetRequest(client, REAL_ESTATE_COMPONENT_JSON + "/" + TEST_BUILDING_ID);
  }

  @Test
  void testCreate() {
    client.when(postRequest(REAL_ESTATE_COMPONENT_JSON))
        .respond(okResponse(objectToJson(TEST_BUILDING)));

    realEstateComponentService.createBuilding(TEST_BUILDING);

    verifyPostRequest(client, REAL_ESTATE_COMPONENT_JSON, objectToJson(TEST_BUILDING));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(REAL_ESTATE_COMPONENT_JSON))
        .respond(okResponse(objectToJson(TEST_BUILDING)));

    realEstateComponentService.updateBuilding(TEST_BUILDING);

    verifyPutRequest(client, REAL_ESTATE_COMPONENT_JSON, objectToJson(TEST_BUILDING));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(REAL_ESTATE_COMPONENT_JSON + "/" + TEST_BUILDING_ID))
        .respond(response().withStatusCode(OK));

    realEstateComponentService.deleteBuilding(UUID.fromString(TEST_BUILDING_ID));

    verifyDeleteRequest(client, REAL_ESTATE_COMPONENT_JSON + "/" + TEST_BUILDING_ID);
  }

}
