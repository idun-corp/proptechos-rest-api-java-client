package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.SENSOR_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildSensor;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.Sensor;
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
public class SensorServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedSensors();
  private static final String TEST_SENSOR_ID = "14f883c7-eda5-40a1-8280-002ffb237400";
  private static final Sensor TEST_SENSOR = buildSensor();

  private static SensorService sensorService;
  private final MockServerClient client;

  SensorServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    sensorService = serviceFactory.sensorService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + SENSOR_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + SENSOR_JSON + "/" + TEST_SENSOR_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(SENSOR_JSON)).respond(okResponse(PAGED_DATA));

    sensorService.getFirstPage(50);

    verifyGetRequest(client, SENSOR_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(SENSOR_JSON)).respond(okResponse(PAGED_DATA));

    sensorService.getLastPage(50);

    verifyGetRequest(client, SENSOR_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));

    verifyGetRequest(client, SENSOR_JSON,
        Parameter.param("page", "101"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(SENSOR_JSON)).respond(okResponse(PAGED_DATA));

    sensorService.getPage(1, 50);

    verifyGetRequest(client, SENSOR_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(SENSOR_JSON)).respond(okResponse(PAGED_DATA));

    Paged<Sensor> paged = sensorService.getFirstPage(50);
    sensorService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, SENSOR_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));

    verifyGetRequest(client, SENSOR_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(SENSOR_JSON + "/" + TEST_SENSOR_ID))
        .respond(okResponse(DataLoader.loadSingleSensor()));

    sensorService.getById(UUID.fromString(TEST_SENSOR_ID));

    verifyGetRequest(client, SENSOR_JSON + "/" + TEST_SENSOR_ID);
  }

  @Test
  void testCreate() {
    client.when(postRequest(SENSOR_JSON))
        .respond(okResponse(objectToJson(TEST_SENSOR)));

    sensorService.createSensor(TEST_SENSOR);

    verifyPostRequest(client, SENSOR_JSON, objectToJson(TEST_SENSOR));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(SENSOR_JSON))
        .respond(okResponse(objectToJson(TEST_SENSOR)));

    sensorService.updateSensor(TEST_SENSOR);

    verifyPutRequest(client, SENSOR_JSON, objectToJson(TEST_SENSOR));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(SENSOR_JSON + "/" + TEST_SENSOR_ID))
        .respond(response().withStatusCode(OK));

    sensorService.deleteSensor(UUID.fromString(TEST_SENSOR_ID));

    verifyDeleteRequest(client, SENSOR_JSON + "/" + TEST_SENSOR_ID);
  }

}
