package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.DEVICE_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildDevice;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.common.IDevice;
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
public class DeviceServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedDevices();
  private static final String TEST_DEVICE_ID = "5cd99e11-aa99-43b6-b5f5-01a0033aa940";
  private static final IDevice TEST_DEVICE = buildDevice();

  private static DeviceService deviceService;
  private final MockServerClient client;

  DeviceServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    deviceService = serviceFactory.deviceService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + DEVICE_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + DEVICE_JSON + "/" + TEST_DEVICE_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(DEVICE_JSON)).respond(okResponse(PAGED_DATA));

    deviceService.getFirstPage(50);

    verifyGetRequest(client, DEVICE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(DEVICE_JSON)).respond(okResponse(PAGED_DATA));

    deviceService.getLastPage(50);

    verifyGetRequest(client, DEVICE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));

    verifyGetRequest(client, DEVICE_JSON,
        Parameter.param("page", "136"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(DEVICE_JSON)).respond(okResponse(PAGED_DATA));

    deviceService.getPage(32, 50);

    verifyGetRequest(client, DEVICE_JSON,
        Parameter.param("page", "32"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(DEVICE_JSON)).respond(okResponse(PAGED_DATA));

    Paged<IDevice> paged = deviceService.getFirstPage(50);
    deviceService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, DEVICE_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));

    verifyGetRequest(client, DEVICE_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(DEVICE_JSON + "/" + TEST_DEVICE_ID))
        .respond(okResponse(DataLoader.loadSingleDevice()));

    deviceService.getById(UUID.fromString(TEST_DEVICE_ID));

    verifyGetRequest(client, DEVICE_JSON + "/" + TEST_DEVICE_ID);
  }

  @Test
  void testCreate() {
    client.when(postRequest(DEVICE_JSON))
        .respond(okResponse(objectToJson(TEST_DEVICE)));

    deviceService.createDevice(TEST_DEVICE);

    verifyPostRequest(client, DEVICE_JSON, objectToJson(TEST_DEVICE));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(DEVICE_JSON))
        .respond(okResponse(objectToJson(TEST_DEVICE)));

    deviceService.updateDevice(TEST_DEVICE);

    verifyPutRequest(client, DEVICE_JSON, objectToJson(TEST_DEVICE));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(DEVICE_JSON + "/" + TEST_DEVICE_ID))
        .respond(response().withStatusCode(OK));

    deviceService.deleteDevice(UUID.fromString(TEST_DEVICE_ID));

    verifyDeleteRequest(client, DEVICE_JSON + "/" + TEST_DEVICE_ID);
  }

}
