package com.proptechos.service;

import com.proptechos.model.buildingcomponent.Storey;
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

import static com.proptechos.http.constants.ApiEndpoints.STOREYS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.STOREY_JSON;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;

@ExtendWith(WireMockExtension.class)
public class StoreyServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedStoreys();
    private static final String TEST_STOREY_ID = "d92a2a9f-6a4c-4cf0-9ddc-43f5666ebe37";
    private static final Storey TEST_STOREY = buildStorey();

    private static StoreyService storeyService;

    @BeforeAll
    static void setUp() {
        storeyService = serviceFactory.storeyService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "15");

        stubGetResponse(STOREY_JSON, parameters, PAGED_DATA);

        storeyService.getFirstPage(15);

        verifyGetRequest(STOREY_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "4");
        parameters2.put("size", "15");

        stubGetResponse(STOREY_JSON, parameters1, PAGED_DATA);
        stubGetResponse(STOREY_JSON, parameters2, PAGED_DATA);

        storeyService.getLastPage(15);

        verifyGetRequest(STOREY_JSON, parameters1);
        verifyGetRequest(STOREY_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "15");

        stubGetResponse(STOREY_JSON, parameters, PAGED_DATA);

        storeyService.getPage(1, 15);

        verifyGetRequest(STOREY_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "15");

        stubGetResponse(STOREY_JSON, parameters1, PAGED_DATA);
        stubGetResponse(STOREY_JSON, parameters2, PAGED_DATA);

        Paged<Storey> paged = storeyService.getFirstPage(15);
        storeyService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(STOREY_JSON, parameters1);
        verifyGetRequest(STOREY_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(STOREY_JSON + "/" + TEST_STOREY_ID,
            new HashMap<>(), DataLoader.loadSingleStorey());

        storeyService.getById(UUID.fromString(TEST_STOREY_ID));

        verifyGetRequest(STOREY_JSON + "/" + TEST_STOREY_ID, new HashMap<>());
    }

    @Test
    void testCreate() {
        stubPostResponse(STOREY_JSON, objectToJson(TEST_STOREY));

        storeyService.createStorey(TEST_STOREY);

        verifyPostRequest(STOREY_JSON, objectToJson(TEST_STOREY));
    }

    @Test
    void testUpdate() {
        stubPutResponse(STOREY_JSON, objectToJson(TEST_STOREY));

        storeyService.updateStorey(TEST_STOREY);

        verifyPutRequest(STOREY_JSON, objectToJson(TEST_STOREY));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(STOREY_JSON + "/" + TEST_STOREY_ID);

        storeyService.deleteStorey(UUID.fromString(TEST_STOREY_ID));

        verifyDeleteRequest(STOREY_JSON + "/" + TEST_STOREY_ID);
    }

    @Test
    void testCreateBatch() {
        stubPostResponse(STOREYS_JSON, objectToJson(successBatchResponse()));

        storeyService.createStoreys(Arrays.asList(TEST_STOREY, TEST_STOREY));

        verifyPostRequest(STOREYS_JSON, objectToJson(Arrays.asList(TEST_STOREY, TEST_STOREY)));
    }

    @Test
    void testUpdateBatch() {
        stubPutResponse(STOREYS_JSON, objectToJson(successBatchResponse()));

        storeyService.updateStoreys(Arrays.asList(TEST_STOREY, TEST_STOREY));

        verifyPutRequest(STOREYS_JSON, objectToJson(Arrays.asList(TEST_STOREY, TEST_STOREY)));
    }

    @Test
    void testFailedCreateBatch() {
        stubFailedPostResponse(STOREYS_JSON, DataLoader.loadBatchFailedStoreys());

        storeyService.createStoreys(Arrays.asList(TEST_STOREY, TEST_STOREY));

        verifyPostRequest(STOREYS_JSON, objectToJson(Arrays.asList(TEST_STOREY, TEST_STOREY)));
    }

    @Test
    void testFailedUpdateBatch() {
        stubFailedPutResponse(STOREYS_JSON, DataLoader.loadBatchFailedStoreys());

        storeyService.updateStoreys(Arrays.asList(TEST_STOREY, TEST_STOREY));

        verifyPutRequest(STOREYS_JSON, objectToJson(Arrays.asList(TEST_STOREY, TEST_STOREY)));
    }
}
