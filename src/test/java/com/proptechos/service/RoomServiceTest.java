package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ROOM_JSON;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.utils.TestDataHelper.buildRoom;
import static com.proptechos.utils.TestDataHelper.objectToJson;
import static com.proptechos.utils.ValidationUtils.verifyDeleteRequest;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static com.proptechos.utils.ValidationUtils.verifyPostRequest;
import static com.proptechos.utils.ValidationUtils.verifyPutRequest;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.proptechos.model.buildingcomponent.Room;
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
public class RoomServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedRooms();
  private static final String TEST_ROOM_ID = "4297626f-af51-471d-9503-5576a0544440";
  private static final Room TEST_ROOM = buildRoom();
  
  private static RoomService roomService;
  private final MockServerClient client;
  
  RoomServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    roomService = serviceFactory.roomService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + ROOM_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + ROOM_JSON + "/" + TEST_ROOM_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(ROOM_JSON)).respond(okResponse(PAGED_DATA));

    roomService.getFirstPage(50);

    verifyGetRequest(client, ROOM_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(ROOM_JSON)).respond(okResponse(PAGED_DATA));

    roomService.getLastPage(50);

    verifyGetRequest(client, ROOM_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));

    verifyGetRequest(client, ROOM_JSON,
        Parameter.param("page", "48"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(ROOM_JSON)).respond(okResponse(PAGED_DATA));

    roomService.getPage(1, 50);

    verifyGetRequest(client, ROOM_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(ROOM_JSON)).respond(okResponse(PAGED_DATA));

    Paged<Room> paged = roomService.getFirstPage(50);
    roomService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, ROOM_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "50"));

    verifyGetRequest(client, ROOM_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "50"));
  }

  @Test
  void testGetById() {
    client.when(getRequest(ROOM_JSON + "/" + TEST_ROOM_ID))
        .respond(okResponse(DataLoader.loadSingleRoom()));

    roomService.getById(UUID.fromString(TEST_ROOM_ID));

    verifyGetRequest(client, ROOM_JSON + "/" + TEST_ROOM_ID);
  }

  @Test
  void testCreate() {
    client.when(postRequest(ROOM_JSON))
        .respond(okResponse(objectToJson(TEST_ROOM)));

    roomService.createRoom(TEST_ROOM);

    verifyPostRequest(client, ROOM_JSON, objectToJson(TEST_ROOM));
  }

  @Test
  void testUpdate() {
    client.when(putRequest(ROOM_JSON))
        .respond(okResponse(objectToJson(TEST_ROOM)));

    roomService.updateRoom(TEST_ROOM);

    verifyPutRequest(client, ROOM_JSON, objectToJson(TEST_ROOM));
  }

  @Test
  void testDelete() {
    client.when(deleteRequest(ROOM_JSON + "/" + TEST_ROOM_ID))
        .respond(response().withStatusCode(OK));

    roomService.deleteRoom(UUID.fromString(TEST_ROOM_ID));

    verifyDeleteRequest(client, ROOM_JSON + "/" + TEST_ROOM_ID);
  }

}
