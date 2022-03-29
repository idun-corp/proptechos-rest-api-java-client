package com.proptechos.service;

import com.proptechos.model.buildingcomponent.Room;
import com.proptechos.model.common.Paged;
import com.proptechos.utils.DataLoader;
import com.proptechos.utils.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.ROOMS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.ROOM_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;

@ExtendWith(WireMockExtension.class)
public class RoomServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedRooms();
    private static final String TEST_ROOM_ID = "4297626f-af51-471d-9503-5576a0544440";
    private static final Room TEST_ROOM = buildRoom();

    private static RoomService roomService;

    @BeforeAll
    static void setUp() {
        roomService = serviceFactory.roomService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "50");

        stubGetResponse(ROOM_JSON, parameters, PAGED_DATA);

        roomService.getFirstPage(50);

        verifyGetRequest(ROOM_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "50");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "48");
        parameters2.put("size", "50");

        stubGetResponse(ROOM_JSON, parameters1, PAGED_DATA);
        stubGetResponse(ROOM_JSON, parameters2, PAGED_DATA);

        roomService.getLastPage(50);

        verifyGetRequest(ROOM_JSON, parameters1);
        verifyGetRequest(ROOM_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "50");

        stubGetResponse(ROOM_JSON, parameters, PAGED_DATA);

        roomService.getPage(1, 50);

        verifyGetRequest(ROOM_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "50");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "50");

        stubGetResponse(ROOM_JSON, parameters1, PAGED_DATA);
        stubGetResponse(ROOM_JSON, parameters2, PAGED_DATA);

        Paged<Room> paged = roomService.getFirstPage(50);
        roomService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(ROOM_JSON, parameters1);
        verifyGetRequest(ROOM_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(ROOM_JSON + "/" + TEST_ROOM_ID,
            new HashMap<>(), DataLoader.loadSingleRoom());

        roomService.getById(UUID.fromString(TEST_ROOM_ID));

        verifyGetRequest(ROOM_JSON + "/" + TEST_ROOM_ID, new HashMap<>());
    }

    @Test
    void testCreate() {
        stubPostResponse(ROOM_JSON, objectToJson(TEST_ROOM));

        roomService.createRoom(TEST_ROOM);

        verifyPostRequest(ROOM_JSON, objectToJson(TEST_ROOM));
    }

    @Test
    void testUpdate() {
        stubPutResponse(ROOM_JSON, objectToJson(TEST_ROOM));

        roomService.updateRoom(TEST_ROOM);

        verifyPutRequest(ROOM_JSON, objectToJson(TEST_ROOM));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(ROOM_JSON + "/" + TEST_ROOM_ID);

        roomService.deleteRoom(UUID.fromString(TEST_ROOM_ID));

        verifyDeleteRequest(ROOM_JSON + "/" + TEST_ROOM_ID);
    }

    @Test
    void testCreateBatch() {
        stubPostResponse(ROOMS_JSON, objectToJson(successBatchResponse()));

        roomService.createRooms(Arrays.asList(TEST_ROOM, TEST_ROOM));

        verifyPostRequest(ROOMS_JSON, objectToJson(Arrays.asList(TEST_ROOM, TEST_ROOM)));
    }

    @Test
    void testUpdateBatch() {
        stubPutResponse(ROOMS_JSON, objectToJson(successBatchResponse()));

        roomService.updateRooms(Arrays.asList(TEST_ROOM, TEST_ROOM));

        verifyPutRequest(ROOMS_JSON, objectToJson(Arrays.asList(TEST_ROOM, TEST_ROOM)));
    }

    @Test
    void testFailedCreateBatch() {
        stubFailedPostResponse(ROOMS_JSON, DataLoader.loadBatchFailedRooms());

        roomService.createRooms(Arrays.asList(TEST_ROOM, TEST_ROOM));

        verifyPostRequest(ROOMS_JSON, objectToJson(Arrays.asList(TEST_ROOM, TEST_ROOM)));
    }

    @Test
    void testFailedUpdateBatch() {
        stubFailedPutResponse(ROOMS_JSON, DataLoader.loadBatchFailedRooms());

        roomService.updateRooms(Arrays.asList(TEST_ROOM, TEST_ROOM));

        verifyPutRequest(ROOMS_JSON, objectToJson(Arrays.asList(TEST_ROOM, TEST_ROOM)));
    }

    @Test
    void testGetHistory() {
        stubGetResponse(getHistoryEndpoint(ROOM_JSON, UUID.fromString(TEST_ROOM_ID)),
            new HashMap<>(), objectToJson(buildTwinHistory(TEST_ROOM)));

        roomService.getHistory(UUID.fromString(TEST_ROOM_ID), null, null);

        verifyGetRequest(getHistoryEndpoint(ROOM_JSON, UUID.fromString(TEST_ROOM_ID)), new HashMap<>());
    }

}
