package com.proptechos.service;

import com.proptechos.model.System;
import com.proptechos.model.common.Paged;
import com.proptechos.utils.DataLoader;
import com.proptechos.utils.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.SYSTEM_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(WireMockExtension.class)
public class SystemServiceTest extends BaseServiceTest {


    private static final String PAGED_DATA = DataLoader.loadPagedSystem();
    private static final String TEST_SYSTEM_ID = "4e919e71-61c0-4d44-b240-5308adfd332f";
    private static final System TEST_SYSTEM = buildSystem();
    private static final UUID[] TEST_ID_LIST = new UUID[]{
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()
    };

    private static SystemService systemService;

    @BeforeAll
    static void setUp() {
        systemService = serviceFactory.systemService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "15");

        stubGetResponse(SYSTEM_JSON, parameters, PAGED_DATA);

        systemService.getFirstPage(15);

        verifyGetRequest(SYSTEM_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "2");
        parameters2.put("size", "15");

        stubGetResponse(SYSTEM_JSON, parameters1, PAGED_DATA);
        stubGetResponse(SYSTEM_JSON, parameters2, PAGED_DATA);

        systemService.getLastPage(15);

        verifyGetRequest(SYSTEM_JSON, parameters1);
        verifyGetRequest(SYSTEM_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "15");

        stubGetResponse(SYSTEM_JSON, parameters, PAGED_DATA);

        systemService.getPage(1, 15);

        verifyGetRequest(SYSTEM_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "15");

        stubGetResponse(SYSTEM_JSON, parameters1, PAGED_DATA);
        stubGetResponse(SYSTEM_JSON, parameters2, PAGED_DATA);

        Paged<System> paged = systemService.getFirstPage(15);
        systemService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(SYSTEM_JSON, parameters1);
        verifyGetRequest(SYSTEM_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(SYSTEM_JSON + "/" + TEST_SYSTEM_ID,
                new HashMap<>(), DataLoader.loadSingleSystem());

        systemService.getById(UUID.fromString(TEST_SYSTEM_ID));

        verifyGetRequest(SYSTEM_JSON + "/" + TEST_SYSTEM_ID, new HashMap<>());
    }

    @Test
    void testGetIncludedAxioms() {
        String requestPath = SYSTEM_JSON + "/" + TEST_SYSTEM_ID + "/includes";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "10");

        stubGetResponse(requestPath, parameters, DataLoader.loadPagedDevices());

        systemService.getIncludedAxioms(
                UUID.fromString(TEST_SYSTEM_ID), 0, 10);

        verifyGetRequest(requestPath, parameters);
    }

    @Test
    void testCreate() {
        stubPostResponse(SYSTEM_JSON, objectToJson(TEST_SYSTEM));

        systemService.createSystem(TEST_SYSTEM);

        verifyPostRequest(SYSTEM_JSON, objectToJson(TEST_SYSTEM));
    }

    @Test
    void testUpdate() {
        stubPutResponse(SYSTEM_JSON, objectToJson(TEST_SYSTEM));

        systemService.updateSystem(TEST_SYSTEM);

        verifyPutRequest(SYSTEM_JSON, objectToJson(TEST_SYSTEM));
    }

    @Test
    void testIncludeAxioms() {
        String requestPath = SYSTEM_JSON + "/" + TEST_SYSTEM_ID + "/include";
        stubPutResponse(requestPath, objectToJson(TEST_ID_LIST));

        List<UUID> included = systemService.includeAxioms(UUID.fromString(TEST_SYSTEM_ID),
                TEST_ID_LIST[0], TEST_ID_LIST[1], TEST_ID_LIST[2], TEST_ID_LIST[3], TEST_ID_LIST[4]);

        assertEquals(5, included.size());

        verifyPutRequest(requestPath, objectToJson(TEST_ID_LIST));
    }

    @Test
    void testExcludeAxioms() {
        String requestPath = SYSTEM_JSON + "/" + TEST_SYSTEM_ID + "/exclude";
        stubPutResponse(requestPath, objectToJson(TEST_ID_LIST));

        List<UUID> included = systemService.excludeAxioms(UUID.fromString(TEST_SYSTEM_ID),
                TEST_ID_LIST[0], TEST_ID_LIST[1], TEST_ID_LIST[2], TEST_ID_LIST[3], TEST_ID_LIST[4]);

        assertEquals(5, included.size());

        verifyPutRequest(requestPath, objectToJson(TEST_ID_LIST));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(SYSTEM_JSON + "/" + TEST_SYSTEM_ID);

        systemService.deleteSystem(UUID.fromString(TEST_SYSTEM_ID));

        verifyDeleteRequest(SYSTEM_JSON + "/" + TEST_SYSTEM_ID);
    }

    @Test
    void testGetHistory() {
        stubGetResponse(getHistoryEndpoint(SYSTEM_JSON, UUID.fromString(TEST_SYSTEM_ID)),
            new HashMap<>(), objectToJson(buildTwinHistory(TEST_SYSTEM)));

        systemService.getHistory(UUID.fromString(TEST_SYSTEM_ID), null, null);

        verifyGetRequest(getHistoryEndpoint(SYSTEM_JSON, UUID.fromString(TEST_SYSTEM_ID)), new HashMap<>());
    }

}
