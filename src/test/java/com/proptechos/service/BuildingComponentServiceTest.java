package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.BUILDING_COMPONENT_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildBuildingComponent;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.common.IBuildingComponent;
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
public class BuildingComponentServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedBuildingComponents();
  private static final String TEST_BC_ID = "397abf21-0e94-4bee-9bc9-0464a7ddd44a";
  private static final IBuildingComponent TEST_BC = buildBuildingComponent();
  
  private static BuildingComponentService buildingComponentService;
  private final MockServerClient client;
  
  BuildingComponentServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    buildingComponentService = serviceFactory.buildingComponentService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + BUILDING_COMPONENT_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + BUILDING_COMPONENT_JSON + "/" + TEST_BC_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(BUILDING_COMPONENT_JSON)).respond(okResponse(PAGED_DATA));

    buildingComponentService.getFirstPage(50);

    verifyGetRequest(client, BUILDING_COMPONENT_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(BUILDING_COMPONENT_JSON)).respond(okResponse(PAGED_DATA));

    buildingComponentService.getLastPage(50);

    verifyGetRequest(client, BUILDING_COMPONENT_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));

    verifyGetRequest(client, BUILDING_COMPONENT_JSON,
        Parameter.param("page", "50"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(BUILDING_COMPONENT_JSON)).respond(okResponse(PAGED_DATA));

    buildingComponentService.getPage(1, 50);

    verifyGetRequest(client, BUILDING_COMPONENT_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(BUILDING_COMPONENT_JSON)).respond(okResponse(PAGED_DATA));

    Paged<IBuildingComponent> paged = buildingComponentService.getFirstPage(50);
    buildingComponentService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, BUILDING_COMPONENT_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));

    verifyGetRequest(client, BUILDING_COMPONENT_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(BUILDING_COMPONENT_JSON + "/" + TEST_BC_ID))
        .respond(okResponse(DataLoader.loadSingleBuildingComponent()));

    buildingComponentService.getById(UUID.fromString(TEST_BC_ID));

    verifyGetRequest(client, BUILDING_COMPONENT_JSON + "/" + TEST_BC_ID);
  }

  @Test
  void testCreate() {
    client.when(postRequest(BUILDING_COMPONENT_JSON))
        .respond(okResponse(objectToJson(TEST_BC)));

    buildingComponentService.createBuildingComponent(TEST_BC);

    verifyPostRequest(client, BUILDING_COMPONENT_JSON, objectToJson(TEST_BC));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(BUILDING_COMPONENT_JSON))
        .respond(okResponse(objectToJson(TEST_BC)));

    buildingComponentService.updateBuildingComponent(TEST_BC);

    verifyPutRequest(client, BUILDING_COMPONENT_JSON, objectToJson(TEST_BC));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(BUILDING_COMPONENT_JSON + "/" + TEST_BC_ID))
        .respond(response().withStatusCode(OK));

    buildingComponentService.deleteBuildingComponent(UUID.fromString(TEST_BC_ID));

    verifyDeleteRequest(client, BUILDING_COMPONENT_JSON + "/" + TEST_BC_ID);
  }

}
